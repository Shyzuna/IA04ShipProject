package fr.shipsimulator.agent;

import jade.core.Agent;
import fr.shipsimulator.behaviour.CityExchangeBehaviour;
import fr.shipsimulator.behaviour.CityMissionBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;

public class CityAgent extends Agent implements Constante {
	private static final long serialVersionUID = 1L;
	
	private City city;
	
	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public CityAgent(int x, int y, String name){
		this.city = new City(x, y, name);
	}
	
	public void setup() {
		MainGui.writeLog("City Agent", "New city : "+ this.getLocalName());
		//TODO: ENLEVER DEV TICK
		this.addBehaviour(new CityMissionBehaviour(this, 30000));
		this.addBehaviour(new CityExchangeBehaviour(this));
	}
}
