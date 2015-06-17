package fr.shipsimulator.agent.boatCrew;

import jade.core.AID;
import jade.core.Agent;

public class BoatCrewAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	protected AID myBoat;
	
	public BoatCrewAgent(AID boat) {
		this.myBoat = boat;
	}

	public AID getMyBoat() {
		return myBoat;
	}

	public void setMyBoat(AID myBoat) {
		this.myBoat = myBoat;
	}
}
