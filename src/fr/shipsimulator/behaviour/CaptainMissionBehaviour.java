package fr.shipsimulator.behaviour;

import jade.core.AID;
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

public class CaptainMissionBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;

	private Mission chosenMission;
	private HashMap<Mission, Integer> missionVote;
	private Integer nbElecteur, nbVotant;
	private BoatCaptainAgent myAgent;
		
	public CaptainMissionBehaviour(BoatCaptainAgent a) {
		super(a);
		MainGui.writeLog("CaptainMissionBehaviour", "New Behaviour");
		state = State.NO_MISSION;
		
		MainGui.writeLog("CaptainMissionBehaviour", "Demande des missions disponibles");
		askAvailableMission(myAgent.getCityDeparture());
		state = State.MISSION_LIST_ASKED;
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.MISSION_LIST_ASKED){
			mt = new MessageTemplate(new MissionListResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				missionVote = new HashMap<Mission, Integer>();
				
				List<Mission> missionList = new GenericMessageContent<Mission>().deserialize(msg.getContent());
				for(Mission mission : missionList) {
					missionVote.put(mission, 0);
				}
				
				MainGui.writeLog("CaptainMissionBehaviour", "Demande de la liste d'équipage pour vote");
				askForCrewMembers();
				state = State.OBS_LIST_ASKED;				
			}
		}
		else if(state == State.OBS_LIST_ASKED){
			mt = new MessageTemplate(new CrewListResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				List<AID> crewMembers = new GenericMessageContent<AID>().deserialize(msg.getContent());
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
				Mission chosenMission = new GenericMessageContent<Mission>().deserialize(msg.getContent()).get(0);
				for(Entry<Mission, Integer> entry : missionVote.entrySet()) {
					if(entry.getKey().getId() == chosenMission.getId()){
				    	entry.setValue(entry.getValue() + 1);
				    	nbVotant++;
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
				if(msg.getPerformative() == ACLMessage.AGREE){
					MainGui.writeLog("CaptainMissionBehaviour", "Mission choisie !");			
					myAgent.setCurrentMission(chosenMission);
					myAgent.addBehaviour(new CaptainCommerceBehaviour(myAgent, TypeCommerce.ACHAT));
				}
				else{					
					MainGui.writeLog("CaptainMissionBehaviour", "La mission n'est plus dispo, on recommence");
					// Nouveau behaviour pour recommencer le choix et suppreesion de celui-ci
					myAgent.addBehaviour(new CaptainMissionBehaviour(myAgent));
				}
				done = true;
			}
		}
		block();
	}

	private void askAvailableMission(City city){		
		ACLMessage missionRequest = new ACLMessage(ACLMessage.REQUEST);
		missionRequest.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		missionRequest.setContent("MissionListRequest");
		myAgent.send(missionRequest);
	}
		
	private void askVoteToCrew(List<AID> crewMembers){		
		ACLMessage crewRequest = new ACLMessage(ACLMessage.REQUEST);
		// Envoyer ï¿½ tous les observer
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
}
