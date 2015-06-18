package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.Mission;

public class CaptainCommerceBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
	
	private Mission chosenMission;
	private TypeCommerce tc;
	
	public CaptainCommerceBehaviour(BoatCrewAgent mAgent, TypeCommerce typeCommerce, Mission mission) {
		super(mAgent);
		MainGui.writeLog("CaptainCommerceBehaviour", "New Behaviour");
		state = State.REQUEST_RES;
		this.chosenMission = mission;
		this.tc = typeCommerce;
	}
	
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;

		if(tc == TypeCommerce.ACHAT){
			if(state == State.REQUEST_RES){
				// 1. State = demande achat envoy�e
				ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
				request.addReceiver(new AID(chosenMission.getDeparture().getName() , AID.ISLOCALNAME));
				GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
				mc.content.add(chosenMission.getRessource().ordinal());
				mc.content.add(-1*chosenMission.getResourceAmount());
				request.setContent(mc.serialize());
				MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " souhaite acheter � une ville");
				myAgent.send(request);
				state = State.RESPONSE_RES;
			}
			else{
				mt = new MessageTemplate(new ExchangeResponse());
				ACLMessage request = myAgent.receive(mt);
				if (request != null) {
					if (request.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
						ACLMessage boatInform = new ACLMessage(ACLMessage.INFORM);
						boatInform.addReceiver(((BoatCrewAgent)this.myAgent).getMyBoat());
						GenericMessageContent<Integer> mc2 = new GenericMessageContent<Integer>();
						mc2.content.add(chosenMission.getRessource().ordinal());
						mc2.content.add(chosenMission.getResourceAmount());
						boatInform.setContent("ExchangeInform$:!" + mc2.serialize());
						myAgent.send(boatInform);
						myAgent.addBehaviour(new CaptainDirectionBehaviour((BoatCaptainAgent) myAgent));
					} else {
						MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " exchange with city failed");
					}
				}
				myAgent.addBehaviour(new CaptainDirectionBehaviour((BoatCaptainAgent) myAgent));
				this.done = true;
			}
		}
		else{ // VENTE
			
		}
		
		/*
		// Achat, il faut obtenir de mission la quantit� � r�cup�rer dans la ville, et la ville (localName)
		
		
		// 2. State = r�sultat de la demande
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
		
		// Vente, il faut obtenir de mission la quantit� � vendre � la ville, et la ville (localName)
		
		// 1. State = demande achat envoy�e
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.addReceiver(new AID(cityName , AID.ISLOCALNAME));
		GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
		mc.content.add(typeRes);
		mc.content.add(quantite);
		request.setContent(mc.serialize());
		MainGui.writeLog("CaptainAgent", this.myAgent.getName() + " souhaite vendre � une ville");
		myAgent.send(request);
				
		// 2. State = r�sultat de la demande
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
