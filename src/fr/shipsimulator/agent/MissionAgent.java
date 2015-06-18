package fr.shipsimulator.agent;

import jade.core.Agent;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import fr.shipsimulator.behaviour.MissionBehaviour;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.Mission;
import fr.shipsimulator.structure.Ressource;

public class MissionAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	private EnvironnementAgent ea;
	private List<Mission> missions;
	
	public MissionAgent(EnvironnementAgent env){
		ea = env;
	}
	
	public void setup(){
		missions = new ArrayList<Mission>();
		this.addBehaviour(new MissionBehaviour(this));	
	}
	
	public void addMission(Mission m){
		missions.add(m);
	}
	
	public List<Mission> getMissions(){
		return missions;
	}
	
	public void resetMissions(){
		missions.clear();
		Mission.resetId();
	}
	
	public EnvironnementAgent getEnvironnementAgent(){
		return ea;
	}
	
}
