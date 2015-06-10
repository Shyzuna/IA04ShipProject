package fr.shipsimulator.gui.component;

import jade.core.Agent;
import javafx.event.EventHandler;
import javafx.scene.input.MouseEvent;

public class TileClicked implements EventHandler<MouseEvent>{
	
	private Agent agent;
	
	public TileClicked(Agent agent){
		this.agent = agent;
	}
	
	@Override
	public void handle(MouseEvent event) {
		System.out.println("Tile pressed : " + agent.getLocalName());
        event.consume();
	}

}
