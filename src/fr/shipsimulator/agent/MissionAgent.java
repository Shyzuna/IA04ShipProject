package fr.shipsimulator.agent;

import jade.core.Agent;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import java.util.ArrayList;
import java.util.List;

import fr.shipsimulator.behaviour.MissionBehaviour;
import fr.shipsimulator.structure.Mission;

public class MissionAgent extends Agent {
	private static final long serialVersionUID = 1L;
	
	private EnvironnementAgent ea;
	private List<Mission> missions;
	
	public MissionAgent(EnvironnementAgent env){
		ea = env;
	}
	
	public void setup(){
		// Enregistrement sur le DF
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Mission");
		sd.setName("Mission");
		
		DFAgentDescription dfad = new DFAgentDescription();
		dfad.setName(getAID());
		dfad.addServices(sd);
		try {
			DFService.register(this, dfad);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
				
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
