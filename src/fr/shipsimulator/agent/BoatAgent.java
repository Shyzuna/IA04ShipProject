package fr.shipsimulator.agent;

import jade.core.Agent;
import fr.shipsimulator.behaviour.BoatMovingBehaviour;
import fr.shipsimulator.behaviour.BoatCrewListBehaviour;
import fr.shipsimulator.behaviour.BoatDestructionBehaviour;
import fr.shipsimulator.behaviour.BoatExchangeBehaviour;
import fr.shipsimulator.behaviour.BoatFightingBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.Player;

public class BoatAgent extends Agent implements Constante {
	private static final long serialVersionUID = 1L;
	
	public enum CrewType {OBSERVER, CAPTAIN, GUNNER}

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
		this.addBehaviour(new BoatMovingBehaviour(this));
		this.addBehaviour(new BoatDestructionBehaviour(this, SIMULATION_TICK));
		this.addBehaviour(new BoatCrewListBehaviour(this));
		this.addBehaviour(new BoatFightingBehaviour(this));
		this.addBehaviour(new BoatExchangeBehaviour(this));
	}

}
