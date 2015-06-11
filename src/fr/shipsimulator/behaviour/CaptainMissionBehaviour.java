package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.Mission;

public class CaptainMissionBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;
	
	private Mission	mission;
	
	public CaptainMissionBehaviour() {
		MainGui.writeLog("CaptainMissionBehaviour", "Demande des missions disponibles");
		askAvailableMission(new City(0, 0));
		
		MainGui.writeLog("CaptainMissionBehaviour", "Vote pour choisir une mission");
	}
	
	@Override
	public void action() {
		MessageTemplate mt = new MessageTemplate(new MissionResponse());
		ACLMessage request = myAgent.receive(mt);
		if (request != null) {
			
		}
		else block();
		
	}

	@Override
	public boolean done() {
		return false;
	}	
	
	private void askAvailableMission(City city){		
		ACLMessage envRequest = new ACLMessage(ACLMessage.REQUEST);
		envRequest.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		myAgent.send(envRequest);
	}
	
	private void submitVoteToCrew(GenericMessageContent<String> missions){		
		ACLMessage envRequest = new ACLMessage(ACLMessage.REQUEST);
		envRequest.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		myAgent.send(envRequest);
	}
	
	private class MissionRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MissionRequest$:!(.*)") && (msg.getPerformative() == ACLMessage.REQUEST);
	    }
	}
	
	private class MissionResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MissionResponse(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
}
