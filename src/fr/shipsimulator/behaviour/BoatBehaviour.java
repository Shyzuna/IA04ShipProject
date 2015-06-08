package fr.shipsimulator.behaviour;

import java.util.List;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BoatBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatBehaviour(Agent a, long period) {
		super(a, period);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void onTick() {
		MessageTemplate mt = new MessageTemplate(new MovingRequest());
		ACLMessage request = myAgent.receive(mt);
		if (request != null) {
			String [] split = request.getContent().split(":");
			Boolean correctReq = false;
			if (split[1] != null) {
				List<Integer> reqPosition = new GenericMessageContent<Integer>().deserialize(split[1]);
				if (reqPosition.size() == 2) {
					ACLMessage envRequest = new ACLMessage();
					envRequest.setPerformative(ACLMessage.REQUEST);
					envRequest.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
					GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
					mc.content = reqPosition;
					envRequest.setContent("BoatMove:" + mc.serialize());
					myAgent.send(envRequest);
					correctReq = true;
				}
			}
			if (!correctReq) {
				ACLMessage refuseReq = request.createReply();
				refuseReq.setPerformative(ACLMessage.REFUSE);
				refuseReq.setContent("BoatMoveRefused");
				myAgent.send(refuseReq);
			}
		}
		MessageTemplate mtRep = new MessageTemplate(new MovingResponse());
		ACLMessage response = myAgent.receive(mt);
		if (response != null) {
			AID captainAID = boat.getCaptainAID();
			if (captainAID == null) {
				try {
					throw new Exception("error");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (response.getPerformative() == ACLMessage.REFUSE) {
				ACLMessage refuseReq = new ACLMessage();
				refuseReq.addReceiver(captainAID);
				refuseReq.setPerformative(ACLMessage.REFUSE);
				refuseReq.setContent("BoatMoveRefused");
				myAgent.send(refuseReq);
			} else {
				String [] split = response.getContent().split(":");
				if (split[1] != null) {
					List<Integer> reqPosition = new GenericMessageContent<Integer>().deserialize(split[1]);
					if (reqPosition.size() == 2) {
						boat.setPosX(reqPosition.get(0));
						boat.setPosY(reqPosition.get(1));
					}
				}
				ACLMessage acceptReq = new ACLMessage();
				acceptReq.addReceiver(captainAID);
				acceptReq.setPerformative(ACLMessage.AGREE);
				acceptReq.setContent("BoatMoveAccepted");
				myAgent.send(acceptReq);
			}
		}
		
		// Attendre des ordres de l'�quipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer � env le bateau concern�, nombre impacts et d�gats
		
		block();
	}
	
	private class MovingRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MovingRequest:(.*)") && msg.getPerformative() == ACLMessage.REQUEST;
	    }
	}
	
	private class MovingResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("MovingResponse(.*)") && (msg.getPerformative() == ACLMessage.AGREE || msg.getPerformative() == ACLMessage.REFUSE);
	    }
	}

}
