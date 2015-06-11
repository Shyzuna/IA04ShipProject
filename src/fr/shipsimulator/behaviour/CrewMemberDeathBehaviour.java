package fr.shipsimulator.behaviour;

import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

public class CrewMemberDeathBehaviour extends OneShotBehaviour {
	private static final long serialVersionUID = 1L;
	
	public CrewMemberDeathBehaviour (Agent a){
		super(a);
	}
	
	public void action() {
		// TODO : Inform other crew members of my death
		this.myAgent.doDelete();
	}
}
