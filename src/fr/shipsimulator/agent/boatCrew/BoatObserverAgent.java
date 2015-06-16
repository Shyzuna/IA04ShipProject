package fr.shipsimulator.agent.boatCrew;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.behaviour.ObserverObservBehaviour;
import fr.shipsimulator.behaviour.ObserverVoteBehaviour;
import fr.shipsimulator.gui.MainGui;

public class BoatObserverAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private BoatAgent boatAgent;
	
	public BoatObserverAgent(BoatAgent boat){
		super();
		boatAgent = boat;
	}
	
	public void setup(){
		this.addBehaviour(new ObserverObservBehaviour());
		this.addBehaviour(new ObserverVoteBehaviour());
		MainGui.writeLog("BoatObserverAgent", this.getLocalName() + " prend les commandesready");
	}
	
	public BoatAgent getBoatAgent(){
		return boatAgent;
	}
}
