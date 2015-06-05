package fr.shipsimulator.behaviour;

import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.Player;
import jade.core.behaviours.CyclicBehaviour;

public class BoatBehaviour extends CyclicBehaviour {
	
	public BoatBehaviour() {
		// Creer des agents membre d'équipage et les ajouter au bateau
	}
	
	public void action() {
		// 1. Attendre des ordres de l'équipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer à env le bateau concerné, nombre impacts et dégats
			// 1.2 Attendre un ordre de mouvement -> changer X/Y (information env auto après)
			// Request, from crewMember, json Order, type = combat ou mouvement
		
		// 4. Engager un combat sur demande d'environnement (subir des dégats)
					// -> recevoir d'env le nombre d'impacts et les degats
		
		// 2. Informer un membre d'équipage de la composition de l'équipage sur demande
			// Il faudrait synchroniser ça avec un DFAgentDescription à voir, voir ne garder que ça
			// Eventuellement aussi avoir un behaviour rien que pour ça, parce qu'en fait ça pourrait rester cyclic ça !
		
		// 3. Informer l'agent environnement de sa position, à chaque fois
			// -> inform vers env
		
		// 5. Echanger avec une ville (+ agent mission ?), sur demande
			// A voir ça quand ville et mission seront écrits
		
		// 6. Informer l'agent environnement en cas de destruction
			// verifier si boat.isDestroyed() et inform env
	}

}
