package fr.shipsimulator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import fr.shipsimulator.agent.EnvironnementAgent;

public class EnvironnementBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private EnvironnementAgent ea;
	
	public EnvironnementBehaviour() {
		ea = (EnvironnementAgent) this.myAgent;
	}
		
	@Override
	public void action() {
		// Pour ne pas faire monter le cpu en dev
		block();
	}

}
