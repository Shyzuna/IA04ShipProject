package fr.shipsimulator.agent;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import fr.shipsimulator.behaviour.EnvironnementBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;

public class EnvironnementAgent extends GuiAgent implements Constante{
	
	protected MainGui mainGui;

	public MainGui getMainGui() {
		return mainGui;
	}

	public void setMainGui(MainGui mainGui) {
		this.mainGui = mainGui;
	}

	public EnvironnementAgent() {

	}

	@Override
	protected void setup() {
		//create and launch interface
		mainGui = new MainGui();
		MainGui.setMyAgent(this);
		new Thread(mainGui).start();
		
		this.addBehaviour(new EnvironnementBehaviour());
	}
	
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		//handle Event from gui
		switch(ev.getType()){
			case 1:
				System.out.println("nya");
		}
	}
}
