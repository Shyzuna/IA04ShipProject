package fr.shipsimulator.agent.boatCrew;

import fr.shipsimulator.behaviour.CaptainDirectionBehaviour;
import fr.shipsimulator.behaviour.CaptainMissionBehaviour;
import fr.shipsimulator.gui.MainGui;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		this.addBehaviour(new CaptainMissionBehaviour());
		
		this.addBehaviour(new CaptainDirectionBehaviour());
		MainGui.writeLog("BoatCaptainAgent", this.getLocalName() + " prend les commandes");
	}
}