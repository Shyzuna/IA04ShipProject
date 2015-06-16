package fr.shipsimulator.behaviour;

import java.util.List;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BoatMovingBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatMovingBehaviour(Agent a) {
		super(a);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void action() {
		MessageTemplate mt = new MessageTemplate(new MovingRequest());
		ACLMessage request = myAgent.receive(mt);
		if (request != null) {
			String [] split = request.getContent().split("$:!");
			Boolean correctReq = false;
			MainGui.writeLog("BoatAgent", this.myAgent.getName() + " received a moving request");
			if (split[1] != null) {
				List<Integer> reqPosition = new GenericMessageContent<Integer>().deserialize(split[1]);
				if (reqPosition.size() == 2) {
					ACLMessage envRequest = new ACLMessage(ACLMessage.REQUEST);
					envRequest.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
					GenericMessageContent<Integer> mc = new GenericMessageContent<Integer>();
					mc.content.add(boat.getPosX());
					mc.content.add(boat.getPosY());
					mc.content.add(reqPosition.get(0));
					mc.content.add(reqPosition.get(1));
					envRequest.setContent("BoatMove$:!" + mc.serialize());
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
		ACLMessage response = myAgent.receive(mtRep);
		if (response != null) {
			AID captainAID = boat.getCaptainAID();
			if (captainAID == null) {
				try {
					throw new Exception("Boat has no captain !");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (response.getPerformative() == ACLMessage.REFUSE) {
				ACLMessage refuseReq = new ACLMessage(ACLMessage.REFUSE);
				refuseReq.addReceiver(captainAID);
				refuseReq.setContent("BoatMoveRefused");
				myAgent.send(refuseReq);
			} else {
				String [] split = response.getContent().split(":");
				Boolean correctReq = false;
				if (split[1] != null) {
					List<Integer> reqPosition = new GenericMessageContent<Integer>().deserialize(split[1]);
					if (reqPosition.size() == 2) {
						MainGui.writeLog("BoatAgent", this.myAgent.getName() + " move to " + reqPosition.get(0) + " " + reqPosition.get(1));
						boat.setPosX(reqPosition.get(0));
						boat.setPosY(reqPosition.get(1));
						correctReq = true;
					}
				}
				if (correctReq == true) {
					ACLMessage acceptReq = new ACLMessage(ACLMessage.AGREE);
					acceptReq.addReceiver(captainAID);
					acceptReq.setContent("BoatMoveAccepted$:!" + split[1]);
					myAgent.send(acceptReq);
				} else {
					ACLMessage refuseReq = request.createReply();
					refuseReq.setPerformative(ACLMessage.REFUSE);
					refuseReq.setContent("BoatMoveRefused");
					myAgent.send(refuseReq);
				}
			}
		}
		block();
	}
	
	private class MovingRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("(.*)BoatCaptainAgent(.*)") && msg.getContent().matches("MovingRequest$:!(.*)") && msg.getPerformative() == ACLMessage.REQUEST;
	    }
	}
	
	private class MovingResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("(.*)EnvironnementAgent(.*)") && msg.getContent().matches("MovingResponse(.*)") && (msg.getPerformative() == ACLMessage.AGREE || msg.getPerformative() == ACLMessage.REFUSE);
	    }
	}

}
