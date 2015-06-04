package fr.shipsimulator.agent;

import jade.core.Agent;
import fr.shipsimulator.behaviour.BoatCrewBehaviour;
import fr.shipsimulator.gui.MainGui;

public class BoatCrewAgent extends Agent{
	private static final long serialVersionUID = 1L;

	@Override
	protected void setup() {
		super.setup();
		
		MainGui.writeLog("BoatCrew Agent", "New Equipage Member : "+ this.getLocalName());
		this.addBehaviour(new BoatCrewBehaviour(null));
	}
	
	
}
