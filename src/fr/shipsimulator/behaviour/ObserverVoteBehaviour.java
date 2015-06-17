package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;


public class ObserverVoteBehaviour extends CrewMainBehaviour{
	public ObserverVoteBehaviour(BoatCrewAgent ag) {
		super(ag);
		// TODO Auto-generated constructor stub
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		block();
		
	}

}
