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
		
		// 1. Attendre des ordres de l'équipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer à env le bateau concerné, nombre impacts et dégats
			// 1.2 Attendre un ordre de mouvement -> changer X/Y (information env auto après)
			// Request, from crewMember, json Order, type = combat ou mouvement
		
		//CYCLIC// 4. Engager un combat sur demande d'environnement (subir des dégats)
					// -> recevoir d'env le nombre d'impacts et les degats
		
		//CYCLIC// 2. Informer un membre d'équipage de la composition de l'équipage sur demande
			// Il faudrait synchroniser ça avec un DFAgentDescription à voir, voir ne garder que ça
			// Eventuellement aussi avoir un behaviour rien que pour ça, parce qu'en fait ça pourrait rester cyclic ça !
		
		// 3. Informer l'agent environnement de sa position, à chaque mouvement
			// -> inform vers env
		
		// Ordre mouvement (Position communiquée) -> Demander à env si possible
		//
		//
		
		
		// Echanger avec une ville (+ agent mission ?), sur demande
			// TODO quand ville et mission seront écrits
		
		block();
	}

}
