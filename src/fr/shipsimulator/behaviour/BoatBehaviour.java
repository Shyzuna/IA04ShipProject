package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;

public class BoatBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatBehaviour(Agent a, long period) {
		super(a, period);
	}
	
	public void onTick() {
		Boat boat = ((BoatAgent)this.myAgent).getBoat();
		
		// 1. Attendre des ordres de l'�quipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer � env le bateau concern�, nombre impacts et d�gats
			// 1.2 Attendre un ordre de mouvement -> changer X/Y (information env auto apr�s)
			// Request, from crewMember, json Order, type = combat ou mouvement
		

		// Ordre mouvement (Position communiqu�e) -> Demander � env si possible

		
		block();

	}

}
