package fr.shipsimulator.behaviour;

import java.util.List;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BoatFightingBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatFightingBehaviour(Agent a) {
		super(a);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void action() {
		MessageTemplate mt = new MessageTemplate(new TakingDamageInform());
		ACLMessage inform = myAgent.receive(mt);
		//CYCLIC// 4. Engager un combat sur demande d'environnement (subir des dégats)
				// -> recevoir d'env le nombre d'impacts et les degats
		if (inform != null) {
			String [] split = inform.getContent().split("$:!");
			Boolean correctReq = false;
			if (split[1] != null) {
				List<Integer> reqDamage = new GenericMessageContent<Integer>().deserialize(split[1]);
				if (reqDamage.size() == 2) {
					boat.damage(reqDamage.get(0), reqDamage.get(1));
					correctReq = true;
				}
			}
			if (!correctReq) {
				try {
					throw new Exception("Invalid taking damage request from environnement");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
		MessageTemplate mt2 = new MessageTemplate(new FightingRequest());
		ACLMessage request = myAgent.receive(mt2);
		// Attendre des ordres de l'équipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer à env le bateau concerné, nombre impacts et dégats
		if (request != null) {
			String [] split = inform.getContent().split("$:!");
			Boolean correctInf = false;
			int impactCount = 0;
			int damageGiven = 0;
			if (split[1] != null) {
				List<AID> reqAttack = new GenericMessageContent<AID>().deserialize(split[1]);
				if (reqAttack.size() == 1) {
					damageGiven = boat.fire(0.0, 0.0, impactCount);
					ACLMessage envInform = new ACLMessage(ACLMessage.INFORM);
					envInform.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
					GenericMessageContent<AID> mcAid = new GenericMessageContent<AID>();
					mcAid.content.add(reqAttack.get(0));
					GenericMessageContent<Integer> mcDam = new GenericMessageContent<Integer>();
					mcDam.content.add(damageGiven);
					mcDam.content.add(impactCount);
					envInform.setContent("BoatAttack$:!" + mcAid.serialize() + "$:!" + mcDam.serialize());
					myAgent.send(envInform);
					correctInf = true;
				}
			}
			ACLMessage response = request.createReply();
			if (!correctInf) {
				response.setPerformative(ACLMessage.FAILURE);
				response.setContent("FightResponse");
			} else {
				response.setPerformative(ACLMessage.CONFIRM);
				GenericMessageContent<Integer> mcDam = new GenericMessageContent<Integer>();
				mcDam.content.add(damageGiven);
				mcDam.content.add(impactCount);
				response.setContent("FightResponse$:!" + mcDam.serialize());
			}
			this.myAgent.send(response);
		}	
		block();
	}
	
	private class TakingDamageInform implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("(.*)EnvironnementAgent(.*)") && msg.getContent().matches("TakeDamage$:!(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
	
	private class FightingRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("(.*)Boat[COG](.*)Agent(.*)") && msg.getContent().matches("FightingRequest(.*)") && msg.getPerformative() == ACLMessage.REQUEST;
	    }
	}
}
