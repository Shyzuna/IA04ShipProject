package fr.shipsimulator.behaviour;

import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.constantes.Constante.Direction;

public abstract class CrewMainBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;

	protected boolean done = false;
	protected Direction lastDirection;
	
	protected void askForCrewMembers(){
		ACLMessage memberRequest = new ACLMessage(ACLMessage.REQUEST);
		memberRequest.addReceiver(((BoatCaptainAgent) myAgent).getMyBoat());
		memberRequest.setContent("CrewListRequest");
		myAgent.send(memberRequest);
	}
}
