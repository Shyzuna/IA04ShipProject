package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.structure.Boat;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

public class BoatFightingBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatFightingBehaviour(Agent a) {
		super(a);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void action() {
		//CYCLIC// 4. Engager un combat sur demande d'environnement (subir des dégats)
		// -> recevoir d'env le nombre d'impacts et les degats
		// aussi calculer dégats infligés ennemie
		// TODO
		// Attendre des ordres de l'équipe sur demande
			// 1.1 Attendre un ordre de combat -> simuler attaque, communiquer à env le bateau concerné, nombre impacts et dégats
				
		block();
	}

}
