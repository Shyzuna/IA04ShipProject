package fr.shipsimulator.agent.boatCrew;

import jade.core.AID;
import fr.shipsimulator.behaviour.CaptainMissionBehaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Mission;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private AID myBoat;
	private Mission	currentMission;
	
	public void setup(AID boat){
		myBoat = boat;
		
		this.addBehaviour(new CaptainMissionBehaviour());
		
		// Crée dans le CaptainMissionBehaviour
		// this.addBehaviour(new CaptainDirectionBehaviour());
		MainGui.writeLog("BoatCaptainAgent", this.getLocalName() + " prend les commandes");
	}

	public AID getMyBoat() {
		return myBoat;
	}

	public void setMyBoat(AID myBoat) {
		this.myBoat = myBoat;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
	}
	
}