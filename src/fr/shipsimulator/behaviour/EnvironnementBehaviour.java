package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.EnvironnementAgent;
import jade.core.behaviours.CyclicBehaviour;

public class EnvironnementBehaviour extends CyclicBehaviour {

	@Override
	public void action() {
		EnvironnementAgent ea = (EnvironnementAgent) this.myAgent;
	}

}
