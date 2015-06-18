package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.GenericMessageContent;

public class BoatCrewListBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatCrewListBehaviour(Agent a) {
		super(a);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void action() {
		MessageTemplate mt = new MessageTemplate(new MatchCrewListRequest());
		ACLMessage request = myAgent.receive(mt);
		if (request != null) {
			ACLMessage reply = request.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			GenericMessageContent<String> mc = new GenericMessageContent<String>();
			for (AID aid : boat.getCrewAIDs()) {
				mc.content.add(aid.getLocalName());
			}
			reply.setContent(Constante.CrewListResponsePattern + mc.serialize());
			MainGui.writeLog("Boat Agent", this.myAgent.getName() + " sends crew list");
			myAgent.send(reply);
		}
		block();
	}
	
	private class MatchCrewListRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches(Constante.CrewListRequestPattern + "(.*)") && msg.getPerformative() == ACLMessage.REQUEST;
	    }
	}
}
