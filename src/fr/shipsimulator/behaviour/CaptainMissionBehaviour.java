package fr.shipsimulator.behaviour;

import jade.core.behaviours.Behaviour;

public class CaptainMissionBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		block();
		
	}

	@Override
	public boolean done() {
		return false;
	}		
}
