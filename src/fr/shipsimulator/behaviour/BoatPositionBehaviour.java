package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

public class BoatPositionBehaviour extends OneShotBehaviour {
	public void action() {
		Boat boat = ((BoatAgent)this.myAgent).getBoat();
		ACLMessage request = new ACLMessage();
		request.setPerformative(ACLMessage.INFORM);
		request.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
		request.setContent("BoatPosition:" + boat.getPosX() + ":" + boat.getPosY());
		myAgent.send(request);
	}

}
