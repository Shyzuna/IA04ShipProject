package fr.shipsimulator.agent.boatCrew;

import jade.core.AID;
import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.behaviour.ObserverObservBehaviour;
import fr.shipsimulator.behaviour.ObserverVoteBehaviour;
import fr.shipsimulator.gui.MainGui;

public class BoatObserverAgent extends BoatCrewAgent{
	private static final long serialVersionUID = 1L;
	
	private BoatAgent boatAgent;
	
	public BoatObserverAgent(AID boat){
		super(boat);
	}
	
	public void setup(){
		this.addBehaviour(new ObserverVoteBehaviour(this));
		this.addBehaviour(new ObserverObservBehaviour(this));
		MainGui.writeLog("BoatObserverAgent", this.getLocalName() + " prend les commandes");
	}
	
	public BoatAgent getBoatAgent(){
		return boatAgent;
	}
}
