package fr.shipsimulator.behaviour;

import java.util.List;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;

public abstract class CrewMainBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;
	
	protected enum State {NO_MISSION, MISSION_LIST_ASKED, OBS_LIST_ASKED, WAIT_FOR_VOTE, WAIT_FOR_CONFIRM, MISSION_OK, 
							PAUSE, WAIT_ALL_OBSERVATIONS, DIRECTION_SENDED};
	
	protected boolean done = false;
	protected State state;
	
	protected List<AID> crewMembers;
	
	
	protected void askForCrewMembers(){
		ACLMessage memberRequest = new ACLMessage(ACLMessage.REQUEST);
		memberRequest.addReceiver(((BoatCaptainAgent) myAgent).getMyBoat());
		memberRequest.setContent("CrewListRequest");
		myAgent.send(memberRequest);
	}
}
