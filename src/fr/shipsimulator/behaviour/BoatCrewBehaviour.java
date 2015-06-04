package fr.shipsimulator.behaviour;

import jade.core.behaviours.Behaviour;
import fr.shipsimulator.structure.BoatCrew;
import fr.shipsimulator.structure.BoatCrew.CrewType;

public class BoatCrewBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;
	
	private BoatCrew boatCrew;
	
	public BoatCrewBehaviour(CrewType type) {
		boatCrew = new BoatCrew(type);
	}
	
	@Override
	public void action() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean done() {
		// TODO Auto-generated method stub
		return false;
	}

}
