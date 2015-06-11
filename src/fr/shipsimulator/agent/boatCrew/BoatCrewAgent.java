package fr.shipsimulator.agent.boatCrew;

import jade.core.Agent;
import fr.shipsimulator.behaviour.ObserverObservBehaviour;
import fr.shipsimulator.behaviour.ObserverVoteBehaviour;
import fr.shipsimulator.gui.MainGui;

public class BoatCrewAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	public void setup(){
		this.addBehaviour(new ObserverObservBehaviour());
		this.addBehaviour(new ObserverVoteBehaviour());
		MainGui.writeLog("BoatCrewAgent", this.getLocalName() + " sur le pont");
	}
	
}
