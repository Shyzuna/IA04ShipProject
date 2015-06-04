package fr.shipsimulator.behaviour;

import jade.core.behaviours.Behaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.BoatCaptain;
import fr.shipsimulator.structure.BoatCrew;
import fr.shipsimulator.structure.BoatObserver;

public class BoatCrewBehaviour extends Behaviour{
	private static final long serialVersionUID = 1L;
	
	
	
	private BoatCrew boatCrew;
	
	public BoatCrewBehaviour(BoatCrew.CrewType type) {
		switch (type) {
		case OBSERVER:
			boatCrew = new BoatObserver();
			break;
		case CAPTAIN:
			boatCrew = new BoatCaptain();
			break;
		default:
			MainGui.writeLog("BoatCreawBehaviour", "Type d'équipage non reconnnu");
			break;
		}
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
