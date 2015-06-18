package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.GenericMessageContent;

public class CaptainCommerceBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
	
	public CaptainCommerceBehaviour(BoatCrewAgent mAgent, TypeCommerce typeCommerce) {
		super(mAgent);
		MainGui.writeLog("CaptainCommerceBehaviour", "New Behaviour");
	}
	
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.MISSION_LIST_ASKED){
			myAgent.addBehaviour(new CaptainDirectionBehaviour((BoatCrewAgent) myAgent));
		}
		
		/*
		// Achat, il faut obtenir de mission la quantité à récupérer dans la ville, et la ville (localName)
		
		// 1. State = demande achat envoyée
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.addReceiver(new AID(cityName , AID.ISLOCALNAME));
		GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
		mc.content.add(typeRes);
		mc.content.add(-quantite);
		request.setContent(mc.serialize());
		MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " souhaite acheter à une ville");
		myAgent.send(request);
		
		// 2. State = résultat de la demande
		MessageTemplate mt = new MessageTemplate(new ExchangeResponse());
		ACLMessage result = myAgent.receive(mt);
		if (request != null) {
			if (result.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				ACLMessage boatInform = new ACLMessage(ACLMessage.INFORM);
				request.addReceiver(((BoatCrewAgent)this.myAgent).getMyBoat(), AID.ISLOCALNAME)));
				GenericMessageContent<Integer> mc2 = new GenericMessageContent<Integer>();
				mc2.content.add(typeRes);
				mc2.content.add(quantite);
				request.setContent("ExchangeInform$:!" + mc2.serialize());
				myAgent.send(request);
			} else {
				MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " exchange with city failed");
			}
		}
		
		// Vente, il faut obtenir de mission la quantité à vendre à la ville, et la ville (localName)
		
		// 1. State = demande achat envoyée
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.addReceiver(new AID(cityName , AID.ISLOCALNAME));
		GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
		mc.content.add(typeRes);
		mc.content.add(quantite);
		request.setContent(mc.serialize());
		MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " souhaite vendre à une ville");
		myAgent.send(request);
				
		// 2. State = résultat de la demande
		MessageTemplate mt = new MessageTemplate(new ExchangeResponse());
		ACLMessage result = myAgent.receive(mt);
		if (request != null) {
			if (result.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
				ACLMessage boatInform = new ACLMessage(ACLMessage.INFORM);
				request.addReceiver(((BoatCrewAgent)this.myAgent).getMyBoat(), AID.ISLOCALNAME)));
				GenericMessageContent<Integer> mc2 = new GenericMessageContent<Integer>();
				mc2.content.add(typeRes);
				mc2.content.add(-quantite);
				request.setContent("ExchangeInform$:!" + mc2.serialize());
				myAgent.send(request);
			} else {
				MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " exchange with city failed");
			}
		}
		*/
		block();
	}
	
	private class ExchangeResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("(.*)City(.*)") && msg.getContent().matches(Constante.ExchangeResponseBehaviour + "(.*)") && (msg.getPerformative() == ACLMessage.ACCEPT_PROPOSAL || msg.getPerformative() == ACLMessage.REJECT_PROPOSAL);
	    }
	}
}
