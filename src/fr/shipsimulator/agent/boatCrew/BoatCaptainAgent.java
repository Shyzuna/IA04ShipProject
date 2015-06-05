package fr.shipsimulator.agent.boatCrew;

import fr.shipsimulator.gui.MainGui;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		this.addBehaviour(new CaptainDirectionBehaviour());
		this.addBehaviour(new CaptainMissionBehaviour());
		MainGui.writeLog("BoatCaptainAgent", this.getLocalName() + " prend les commandes");
	}
}