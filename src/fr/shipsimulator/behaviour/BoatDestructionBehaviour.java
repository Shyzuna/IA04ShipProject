package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class BoatDestructionBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatDestructionBehaviour (Agent a, long period) {
		super(a, period);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	protected void onTick() {
		if (boat.isDestroyed()) {
			ACLMessage request = new ACLMessage(ACLMessage.INFORM);
			request.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
			request.setContent("BoatDestroyed");
			myAgent.send(request);
			boat.killAllCrewMembers();
			MainGui.writeLog("BoatAgent", this.myAgent.getName() + " destroyed !");
			myAgent.doDelete();
		}
	}

}
