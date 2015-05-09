package fr.shipsimulator.agent;

import fr.shipsimulator.behaviour.EnvironnementBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.SimulationGui;
import jade.core.Agent;
import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

public class EnvironnementAgent extends GuiAgent implements Constante{
	
	protected SimulationGui myGui; 
	
	@Override
	protected void setup() {
		
		this.myGui = new SimulationGui(this);
		this.myGui.setVisible(true);
		
		this.addBehaviour(new EnvironnementBehaviour());
	}

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		switch(ev.getType()){
			case QUIT:
		}
	}
}
