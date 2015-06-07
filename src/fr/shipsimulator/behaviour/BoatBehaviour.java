package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
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
	}
	
	public void onTick() {
		Boat boat = ((BoatAgent)this.myAgent).getBoat();
		
		// Attendre des ordres de l'équipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer à env le bateau concerné, nombre impacts et dégats
			// 1.2 Attendre un ordre de mouvement -> changer X/Y (information env auto après)
			// Request, from crewMember, json Order, type = combat ou mouvement
		

		// Ordre mouvement (Position communiquée) -> Demander à env si possible

		// Request move
		ACLMessage request = new ACLMessage();
		request.setPerformative(ACLMessage.REQUEST);
		request.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
		request.setContent("BoatMove:" + boat.getPosX() + ":" + boat.getPosY());
		myAgent.send(request);
		
		MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchSender(new AID("Environnement", AID.ISLOCALNAME)), MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL));
		ACLMessage response = myAgent.receive(mt);
		String[] splited = response.getContent().split(":");
		if (splited.length == 3 && splited[0].equals("BoatMove")) {
			boat.setPosX(Integer.valueOf(splited[1]));
			boat.setPosY(Integer.valueOf(splited[2]));
		}
		
		block();
	}

}
