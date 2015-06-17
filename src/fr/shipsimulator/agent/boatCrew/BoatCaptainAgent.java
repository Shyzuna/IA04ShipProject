package fr.shipsimulator.agent.boatCrew;

import jade.core.AID;
import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.behaviour.CaptainMissionBehaviour;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.Mission;

public class BoatCaptainAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private CityAgent cityDeparture;
	private Mission	currentMission;
	
	public BoatCaptainAgent(AID boat, CityAgent city){
		super(boat);
		this.cityDeparture = city;
		
		// Chose after
		this.currentMission = null;
	}
	
	public void setup(){
		this.addBehaviour(new CaptainMissionBehaviour(this));
		// NB: CaptainDirectionBehaviour cr�e � la fin du CaptainMissionBehaviour
		MainGui.writeLog("BoatCaptainAgent", this.getLocalName() + " prend les commandes");
	}
	
	
	public CityAgent getCityDeparture() {
		return cityDeparture;
	}

	public void setCityDeparture(CityAgent cityDeparture) {
		this.cityDeparture = cityDeparture;
	}

	public Mission getCurrentMission() {
		return currentMission;
	}

	public void setCurrentMission(Mission currentMission) {
		this.currentMission = currentMission;
	}	
}
