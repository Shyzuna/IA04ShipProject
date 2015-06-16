package fr.shipsimulator.agent;

import fr.shipsimulator.behaviour.CityExchangeBehaviour;
import fr.shipsimulator.behaviour.CityMissionBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import jade.core.Agent;

public class CityAgent extends Agent implements Constante {
	private static final long serialVersionUID = 1L;
	
	private City city;
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public CityAgent(int x, int y){
		this.city = new City(x, y);
	}
	
	public void setup() {
		MainGui.writeLog("City Agent", "New city : "+ this.getLocalName());
		this.addBehaviour(new CityMissionBehaviour(this, SIMULATION_TICK));
		this.addBehaviour(new CityExchangeBehaviour(this));
	}
}
