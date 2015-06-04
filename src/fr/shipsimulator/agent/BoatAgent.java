package fr.shipsimulator.agent;

import fr.shipsimulator.behaviour.BoatBehaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.Player;
import jade.core.Agent;

public class BoatAgent extends Agent {
	
	private Boat boat;
	
	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	public BoatAgent(int x, int y){
		this.boat = new Boat(new Player(), x, y);
	}
	
	public void setup() {
		MainGui.writeLog("Boat Agent", "New boat : "+ this.getLocalName());
		this.addBehaviour(new BoatBehaviour());
	}

}
