package fr.shipsimulator.agent.boatCrew;

import jade.core.AID;
import fr.shipsimulator.behaviour.CaptainMissionBehaviour;
import fr.shipsimulator.gui.MainGui;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private AID myBoat;
	
	public void setup(AID boat){
		myBoat = boat;
		
		this.addBehaviour(new CaptainMissionBehaviour());
		
		// Cr�e dans le CaptainMissionBehaviour
		// this.addBehaviour(new CaptainDirectionBehaviour());
		MainGui.writeLog("BoatCaptainAgent", this.getLocalName() + " prend les commandes");
	}

	public AID getMyBoat() {
		return myBoat;
	}

	public void setMyBoat(AID myBoat) {
		this.myBoat = myBoat;
	}
}