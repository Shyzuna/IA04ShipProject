package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class BoatDestructionBehaviour extends TickerBehaviour {

	private Boat boat;
	
	public BoatDestructionBehaviour (Agent a, long period) {
		super(a, period);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	protected void onTick() {
		if (boat.isDestroyed()) {
			
			
			// Detruire membre équipage
			myAgent.doDelete();
		}
	}

}
