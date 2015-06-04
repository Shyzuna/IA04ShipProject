package fr.shipsimulator.behaviour;

import jade.core.behaviours.CyclicBehaviour;

public class BoatBehaviour extends CyclicBehaviour {
	
	public BoatBehaviour() {
		// Creer des agents membre d'�quipage et les ajouter au bateau
	}
	
	public void action() {
		// 1. Attendre des ordres de l'�quipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer � env le bateau concern�, nombre impacts et d�gats
			// 1.2 Attendre un ordre de mouvement -> changer X/Y (information env auto apr�s)
			// Request, from crewMember, json Order, type = combat ou mouvement
		
		// 4. Engager un combat sur demande d'environnement (subir des d�gats)
					// -> recevoir d'env le nombre d'impacts et les degats
		
		// 2. Informer un membre d'�quipage de la composition de l'�quipage sur demande
			// Il faudrait synchroniser �a avec un DFAgentDescription � voir, voir ne garder que �a
			// Eventuellement aussi avoir un behaviour rien que pour �a, parce qu'en fait �a pourrait rester cyclic �a !
		
		// 3. Informer l'agent environnement de sa position, � chaque fois
			// -> inform vers env
		
		// 5. Echanger avec une ville (+ agent mission ?), sur demande
			// A voir �a quand ville et mission seront �crits
		
		// 6. Informer l'agent environnement en cas de destruction
			// verifier si boat.isDestroyed() et inform env
		
		// Pour ne pas faire monter le cpu en dev
		block();
	}

}
