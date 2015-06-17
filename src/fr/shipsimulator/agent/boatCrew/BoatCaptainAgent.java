package fr.shipsimulator.agent.boatCrew;

import jade.core.AID;
import fr.shipsimulator.behaviour.CaptainMissionBehaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.Mission;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private City cityDeparture;
	private Mission	currentMission;
	
	public BoatCaptainAgent(AID boat, City city){
		super(boat);
		this.cityDeparture = city;
		
		// Chose after
		this.currentMission = null;
	}
	
	public void setup(){
		this.addBehaviour(new CaptainMissionBehaviour());
		
		// Cr�e dans le CaptainMissionBehaviour
		// this.addBehaviour(new CaptainDirectionBehaviour());
		MainGui.writeLog("BoatCaptainAgent", this.getLocalName() + " prend les commandes");
	}
	
	
	public City getCityDeparture() {
		return cityDeparture;
	}

	public void setCityDeparture(City cityDeparture) {
		this.cityDeparture = cityDeparture;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
	}	
}