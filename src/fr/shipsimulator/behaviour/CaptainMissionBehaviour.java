package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Map.Entry;

import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.MessageContent;
import fr.shipsimulator.structure.Mission;

public class CaptainMissionBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;

	private Mission chosenMission;
	private HashMap<Mission, Integer> missionVote;
	private Integer nbVotant;
	private BoatCaptainAgent myAgent;
		
	public CaptainMissionBehaviour(BoatCaptainAgent a) {
		super(a);
		myAgent = a;
		MainGui.writeLog(myAgent.getLocalName(), "New MissionBehaviour");
		state = State.NO_MISSION;
		
		// TODO: Pour attendre que la ville envoie, � Enlever
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {e.printStackTrace();}
		
		MainGui.writeLog(myAgent.getLocalName(), "Demande des missions disponibles");
		askAvailableMission(myAgent.getCityDeparture());
		state = State.MISSION_LIST_ASKED;
		nbVotant = 0;
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.MISSION_LIST_ASKED){
			mt = new MessageTemplate(new MissionListResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				MainGui.writeLog(myAgent.getLocalName(), "Missions disponibles re�ues\n\t" + msg.getContent());
				missionVote = new HashMap<Mission, Integer>();
				List<Mission> missionList = new GenericMessageContent<Mission>().deserialize(msg.getContent(),Mission.class);
				for(Mission mission : missionList) {
					missionVote.put(mission, 0);
				}
				askForCrewMembers();
				state = State.OBS_LIST_ASKED;	
				MainGui.writeLog(myAgent.getLocalName(), "Demande de la liste d'�quipage pour vote envoy�e");
			}
		}
		else if(state == State.OBS_LIST_ASKED){
			mt = new MessageTemplate(new CrewListResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				MainGui.writeLog(myAgent.getLocalName(), "Liste d'�quipage re�ue\n\t" + msg.getContent());
				updateCrewMembers(msg.getContent());
				askVoteToCrew(crewMembers);
				state = State.WAIT_FOR_VOTE;
				MainGui.writeLog(myAgent.getLocalName(), "Vote pour choisir une mission ouvert");
			}
		}
		else if(state == State.WAIT_FOR_VOTE){
			mt = new MessageTemplate(new MissionCrewResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				MainGui.writeLog(myAgent.getLocalName(), "Vote re�u de " + msg.getSender());
				Mission chosenMission = new GenericMessageContent<Mission>().deserialize(msg.getContent(),Mission.class).get(0);
				for(Entry<Mission, Integer> entry : missionVote.entrySet()) {
					if(entry.getKey().getId() == chosenMission.getId()){
				    	entry.setValue(entry.getValue() + 1);
				    	nbVotant++;
				    }
				}
				if(nbVotant >= nbCrew-1){
					deduceChosenMission();
					confirmMission();
					MainGui.writeLog(myAgent.getLocalName(), "Demande de confirmation de la mission");
					state = State.WAIT_FOR_CONFIRM;
				}
			}
		}
		else if(state == State.WAIT_FOR_CONFIRM){
			mt = new MessageTemplate(new MissionConfirmeResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {				
				if(msg.getPerformative() == ACLMessage.AGREE){
					MainGui.writeLog(myAgent.getLocalName(), "Confirmation de mission re�ue et accept�e");		
					myAgent.setCurrentMission(chosenMission);
					//myAgent.addBehaviour(new CaptainCommerceBehaviour(myAgent, TypeCommerce.ACHAT));
				}
				else{
					MainGui.writeLog(myAgent.getLocalName(), "Confirmation de mission re�ue et refus�e, on recommence");
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
		missionRequest.addReceiver(missionAgent);
		MessageContent mc = new MessageContent();
		mc.content.add(this.myAgent.getCityDeparture().getPosX());
		mc.content.add(this.myAgent.getCityDeparture().getPosY());
		missionRequest.setContent(mc.serialize());
		myAgent.send(missionRequest);
	}
		
	private void askVoteToCrew(List<String> crewMembers){
		ACLMessage crewRequest = new ACLMessage(ACLMessage.REQUEST);
		// Envoyer � tous les observer
		for (String s : crewMembers)	crewRequest.addReceiver(new AID(s,AID.ISLOCALNAME));
		
		// Creer liste des missions
		GenericMessageContent<Mission> missions = new GenericMessageContent<Mission>();
		for(Entry<Mission, Integer> entry : missionVote.entrySet()) {
		    missions.content.add(entry.getKey());
		}
		
		crewRequest.setContent(MissionVoteRequestPatern + missions.serialize());
		myAgent.send(crewRequest);
	}
	
	private void deduceChosenMission(){
		Entry<Mission, Integer> maxEntry = null;

		// vote capitaine
		Random r = new Random();
		Integer randValue,MaxVote = null;
		Mission missionChoosed = null;
		for(Entry<Mission, Integer> entry : missionVote.entrySet()){
			randValue = r.nextInt()*100;
			if(MaxVote == null || randValue > MaxVote){
				MaxVote = randValue;
				missionChoosed = entry.getKey();
			}
		}			
		
		for (Entry<Mission, Integer> entry : missionVote.entrySet()){
			if(entry.getKey() == missionChoosed){
				entry.setValue(entry.getValue()+1);
			}
		    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) > 0){
		    	maxEntry = entry;
		    }
		}
		chosenMission = maxEntry.getKey();
	}
	
	private void confirmMission(){
		ACLMessage missionRequest = new ACLMessage(ACLMessage.CONFIRM);

		missionRequest.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		
		GenericMessageContent<Mission> mission = new GenericMessageContent<Mission>();
		mission.content.add(chosenMission);

		missionRequest.setContent(mission.serialize());
		myAgent.send(missionRequest);
	}
}
