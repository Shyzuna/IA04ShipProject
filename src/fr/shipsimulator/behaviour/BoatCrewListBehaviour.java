package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BoatCrewListBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatCrewListBehaviour() {
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void action() {
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
		ACLMessage response = myAgent.receive(mt);
		ACLMessage reply = response.createReply();
		GenericMessageContent<AID> mc = new GenericMessageContent<AID>();
		for (AID aid : boat.getCrewAIDs()) {
			mc.content.add(aid);
		}
		reply.setContent(mc.serialize());
		myAgent.send(reply);
		
	}

}
