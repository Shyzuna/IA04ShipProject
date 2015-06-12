package fr.shipsimulator.behaviour;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;

public class CaptainDirectionBehaviour extends TickerBehaviour{
	private static final long serialVersionUID = 1L;
	
	public CaptainDirectionBehaviour(Agent a) {
		super(a, Constante.SIMULATION_TICK);
		MainGui.writeLog("CaptainDirectionBehaviour", "New Behaviour");
	}

	@Override
	protected void onTick() {
		// TODO Auto-generated method stub
		
	}
}
