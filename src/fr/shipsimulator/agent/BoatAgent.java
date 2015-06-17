package fr.shipsimulator.agent;

import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;
import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.agent.boatCrew.BoatObserverAgent;
import fr.shipsimulator.behaviour.BoatCrewListBehaviour;
import fr.shipsimulator.behaviour.BoatDestructionBehaviour;
import fr.shipsimulator.behaviour.BoatExchangeBehaviour;
import fr.shipsimulator.behaviour.BoatFightingBehaviour;
import fr.shipsimulator.behaviour.BoatMovingBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.Player;

public class BoatAgent extends Agent implements Constante {
	private static final long serialVersionUID = 1L;
	
	public enum CrewType {OBSERVER, CAPTAIN, GUNNER}

	private Boat boat;
	private BoatCaptainAgent captain;
	private BoatObserverAgent observer;
	private EnvironnementAgent ea;
	
	public Boat getBoat() {
		return boat;
	}

	public void setBoat(Boat boat) {
		this.boat = boat;
	}

	public BoatAgent(int x, int y, EnvironnementAgent env){
		this.boat = new Boat(new Player(), x, y);
		ea = env;
	}
	
	public void setup() {
		MainGui.writeLog("Boat Agent", "New boat : "+ this.getLocalName());
		this.addBehaviour(new BoatMovingBehaviour(this));
		this.addBehaviour(new BoatDestructionBehaviour(this, SIMULATION_TICK));
		this.addBehaviour(new BoatCrewListBehaviour(this));
		this.addBehaviour(new BoatFightingBehaviour(this));
		this.addBehaviour(new BoatExchangeBehaviour(this));

		int index = Integer.parseInt(this.getLocalName().replaceFirst("Boat", ""));
		captain = new BoatCaptainAgent(this.getAID(), ea.getListCity().get(index).getCity());
		observer = new BoatObserverAgent(this.getAID());
		AgentController agCapitaine;
		AgentController agObserver;
		try {
			agObserver = this.getContainerController().acceptNewAgent("Observeur_"+this.getLocalName(), observer);
			agObserver.start();
			agCapitaine = this.getContainerController().acceptNewAgent("Capitaine_"+this.getLocalName(), captain);
			agCapitaine.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
	}

	public void stopBoat(){
		captain.doDelete();
		observer.doDelete();
	}
	
	public void suspendBoat(){
		captain.doSuspend();
		observer.doSuspend();
	}
	
	public void activateBoat(){
		captain.doActivate();
		observer.doActivate();
	}
}
