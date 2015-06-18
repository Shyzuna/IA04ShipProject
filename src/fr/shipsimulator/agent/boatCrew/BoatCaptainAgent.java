package fr.shipsimulator.agent.boatCrew;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.behaviour.CaptainMissionBehaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.Mission;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private City cityDeparture;
	private Mission	currentMission;
	private BoatAgent boat;
	
	public BoatCaptainAgent(BoatAgent b, City city){
		super(b.getAID());
		boat = b;
		this.cityDeparture = city;
		this.currentMission = null;
	}
	
	public void setup(){
		MainGui.writeLog("BoatCaptain Agent", "New captain : " + this.getLocalName());
		this.addBehaviour(new CaptainMissionBehaviour(this));
		// NB: CaptainDirectionBehaviour cr�e � la fin du CaptainMissionBehaviour
		
	}
	
	public BoatAgent getBoat(){
		return boat;
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
