package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class BoatExchangeBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatExchangeBehaviour (Agent a, long period) {
		super(a, period);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	protected void onTick() {
		//// Echanger avec une ville (+ agent mission ?), sur demande
		// TODO quand ville et mission seront écrits

	}

}
