package fr.shipsimulator.agent;

import jade.core.Agent;
import fr.shipsimulator.behaviour.BoatCrewBehaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.BoatCrew;

public class BoatCrewAgent extends Agent{
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		
		MainGui.writeLog("BoatCrew Agent", "New Equipage Member : "+ this.getLocalName());
		this.addBehaviour(new BoatCrewBehaviour(BoatCrew.CrewType.CAPTAIN)); // juste pour que ça compile ...
	}
	
	
}
