package fr.shipsimulator.behaviour;

import jade.core.behaviours.CyclicBehaviour;

public class BoatCrewListBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		//CYCLIC// 2. Informer un membre d'�quipage de la composition de l'�quipage sur demande
		// Il faudrait synchroniser �a avec un DFAgentDescription � voir, voir ne garder que �a
		// Eventuellement aussi avoir un behaviour rien que pour �a, parce qu'en fait �a pourrait rester cyclic �a !
		
		// Et engager un combat apr�s reception ordre par BoatBehaviour
		// TODO
		
	}

}
