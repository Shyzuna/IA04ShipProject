package fr.shipsimulator.behaviour;

import jade.core.behaviours.CyclicBehaviour;

public class BoatCrewListBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		//CYCLIC// 2. Informer un membre d'équipage de la composition de l'équipage sur demande
		// Il faudrait synchroniser ça avec un DFAgentDescription à voir, voir ne garder que ça
		// Eventuellement aussi avoir un behaviour rien que pour ça, parce qu'en fait ça pourrait rester cyclic ça !
		
		// Et engager un combat après reception ordre par BoatBehaviour
		// TODO
		
	}

}
