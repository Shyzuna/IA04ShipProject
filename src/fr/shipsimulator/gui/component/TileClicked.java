package fr.shipsimulator.gui.component;

import jade.core.Agent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;
import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.gui.MainGui;

public class TileClicked implements EventHandler<MouseEvent>{
	
	private Agent agent;
	
	public TileClicked(Agent agent){
		this.agent = agent;
	}
	
	@Override
	public void handle(MouseEvent event) {
		if(agent.getLocalName().matches("Boat.*"))
			MainGui.writeBoatStatus((BoatAgent)agent);
		else
			MainGui.writeCityStatus((CityAgent)agent);
		//System.out.println("Tile pressed : " + agent.getLocalName());
        event.consume();
	}

}
