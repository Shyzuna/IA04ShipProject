package fr.shipsimulator.agent;

import fr.shipsimulator.behaviour.BoatBehaviour;
import fr.shipsimulator.gui.MainGui;
import jade.core.Agent;

public class BoatAgent extends Agent {
	
	public void setup() {
		MainGui.writeLog("Boat Agent", "New boat : "+ this.getLocalName());
		this.addBehaviour(new BoatBehaviour());
	}

}
