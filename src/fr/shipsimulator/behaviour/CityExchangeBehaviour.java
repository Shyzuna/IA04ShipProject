package fr.shipsimulator.behaviour;

import java.util.List;

import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CityExchangeBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private City city;
	
	public CityExchangeBehaviour(Agent a) {
		super(a);
		city = ((CityAgent)this.myAgent).getCity();
	}
	
	public void action() {
		MessageTemplate mt = new MessageTemplate(new ExchangeRequest());
		ACLMessage request = myAgent.receive(mt);
		if (request != null) {
			Boolean correctReq = false;
			GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
			List<Integer> reqChange = mc.deserialize(request.getContent());
			if (reqChange.size() == 2) {
				correctReq = city.addResource(reqChange.get(1), reqChange.get(0));
			}
			ACLMessage response = request.createReply();
			if (correctReq == true) {
				response.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
				response.setContent("RessourcesExchanged");
			} else {
				response.setPerformative(ACLMessage.REJECT_PROPOSAL);
				response.setContent("RessourcesNotExchanged");
			}
			this.myAgent.send(response);
		}
		block();
	}

	private class ExchangeRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("Capitaine_Boat(.*)") && msg.getPerformative() == ACLMessage.PROPOSE;
	    }
	}
}
