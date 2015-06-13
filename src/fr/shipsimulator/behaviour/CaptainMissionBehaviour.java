package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.Mission;

public class CaptainMissionBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;
	
	private enum State {NO_MISSION, MISSION_LIST_ASKED, OBS_LIST_ASKED, WAIT_FOR_VOTE, WAIT_FOR_CONFIRM, MISSION_OK};

	private State state;
	
	private Mission chosenMission;
	private HashMap<Mission, Integer> missionVote;
	private Integer nbElecteur, nbVotant;
	
	private City currentCity;
	
	public CaptainMissionBehaviour() {
		MainGui.writeLog("CaptainMissionBehaviour", "New Behaviour");
		state = State.NO_MISSION;
		
		MainGui.writeLog("CaptainMissionBehaviour", "Demande des missions disponibles");
		askAvailableMission(currentCity);
		state = State.MISSION_LIST_ASKED;
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.MISSION_LIST_ASKED){
			mt = new MessageTemplate(new MissionResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				missionVote = new HashMap<Mission, Integer>();
				
				String rsp = msg.getContent().split("MissionResponse")[0];
				List<Mission> missionList = new GenericMessageContent<Mission>().deserialize(rsp);
				for(Mission mission : missionList) {
					missionVote.put(mission, 0);
				}
				
				MainGui.writeLog("CaptainMissionBehaviour", "Demande de la liste d'�quipage");
				askForCrewMembers();
				state = State.OBS_LIST_ASKED;				
			}
		}
		else if(state == State.OBS_LIST_ASKED){
			mt = new MessageTemplate(new CrewListResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				String rsp = msg.getContent().split("CrewListResponse")[0];
				List<AID> crewMembers = new GenericMessageContent<AID>().deserialize(rsp);
				nbElecteur = crewMembers.size();
				
				MainGui.writeLog("CaptainMissionBehaviour", "Vote pour choisir une mission");
				askVoteToCrew(crewMembers);
				state = State.WAIT_FOR_VOTE;
			}
		}
		else if(state == State.WAIT_FOR_VOTE){
			mt = new MessageTemplate(new MissionCrewResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				String rsp = msg.getContent().split("MissionCrewResponse")[0];
				Mission chosenMission = new GenericMessageContent<Mission>().deserialize(rsp).get(0);
				for(Entry<Mission, Integer> entry : missionVote.entrySet()) {
					if(entry.getKey().getId() == chosenMission.getId()){
				    	entry.setValue(entry.getValue() + 1);
				    }
				}
				if(nbVotant >= nbElecteur){
					deduceChosenMission();
					confirmMission();
					state = State.WAIT_FOR_CONFIRM;
				}
			}
		}
		else if(state == State.WAIT_FOR_CONFIRM){
			mt = new MessageTemplate(new MissionConfirmeResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				String rsp = msg.getContent().split("MissionConfirmeResponse")[0];
				
				
				if(rsp == "ok"){
					MainGui.writeLog("CaptainMissionBehaviour", "Mission choisie !");			
					((BoatCaptainAgent) myAgent).setCurrentMission(chosenMission);
					myAgent.addBehaviour(new CaptainDirectionBehaviour(myAgent, currentCity));
					state = State.MISSION_OK;
				}
				else{
					state = State.NO_MISSION;
					
					MainGui.writeLog("CaptainMissionBehaviour", "La mission n'est plus dispo, on recommence");
					askAvailableMission(new City(0, 0));
					state = State.MISSION_LIST_ASKED;
				}
			}
		}
		block();
	}

	@Override
	public boolean done() {
		return false;
	}	
	
	private void askAvailableMission(City city){		
		ACLMessage missionRequest = new ACLMessage(ACLMessage.REQUEST);
		missionRequest.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		missionRequest.setContent("MissionListRequest");
		myAgent.send(missionRequest);
	}
	
	private void askForCrewMembers(){
		ACLMessage memberRequest = new ACLMessage(ACLMessage.REQUEST);
		memberRequest.addReceiver(((BoatCaptainAgent) myAgent).getMyBoat());
		memberRequest.setContent("CrewListRequest");
		myAgent.send(memberRequest);
	}
	
	private void askVoteToCrew(List<AID> crewMembers){		
		ACLMessage crewRequest = new ACLMessage(ACLMessage.REQUEST);
		
		// Envoyer � tous les observer
		for (AID aid : crewMembers) {
			crewRequest.addReceiver(aid);
		}
		
		// Creer liste des missions
		GenericMessageContent<Mission> missions = new GenericMessageContent<Mission>();
		for(Entry<Mission, Integer> entry : missionVote.entrySet()) {
		    missions.content.add(entry.getKey());
		}
		
		crewRequest.setContent("MissionVote:" + missions.serialize());
		myAgent.send(crewRequest);
	}
	
	private void deduceChosenMission(){
		Entry<Mission, Integer> maxEntry = null;

		for (Entry<Mission, Integer> entry : missionVote.entrySet()){
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0) maxEntry = entry;
		}
		
		chosenMission = maxEntry.getKey();
	}
	
	private void confirmMission(){
		ACLMessage missionRequest = new ACLMessage(ACLMessage.CONFIRM);

		missionRequest.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		
		GenericMessageContent<Mission> mission = new GenericMessageContent<Mission>();
		mission.content.add(chosenMission);

		missionRequest.setContent("Observer" + mission.serialize());
		myAgent.send(missionRequest);
	}
		
	private class MissionResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MissionResponse(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
	
	private class CrewListResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("CrewListResponse(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
	
	private class MissionCrewResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MissionCrewResponse(.*)") && msg.getPerformative() == ACLMessage.CONFIRM;
	    }
	}
	
	private class MissionConfirmeResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MissionConfirmeResponse(.*)") && msg.getPerformative() == ACLMessage.CONFIRM;
	    }
	}
}
