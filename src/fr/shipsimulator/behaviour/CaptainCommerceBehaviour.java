package fr.shipsimulator.behaviour;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.gui.MainGui;

public class CaptainCommerceBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
	
	public CaptainCommerceBehaviour(BoatCrewAgent mAgent, TypeCommerce typeCommerce) {
		super(mAgent);
		MainGui.writeLog("CaptainCommerceBehaviour", "New Behaviour");
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.MISSION_LIST_ASKED){
			myAgent.addBehaviour(new CaptainDirectionBehaviour(myAgent));
		}
		block();
	}
}
