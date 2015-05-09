package fr.shipsimulator.conteneur;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;

public class SimulationConteneur {
	public static String SECONDARY_PROPERTIES_FILE = "./properties/simulationConf.conf";
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		try {
			p = new ProfileImpl(SECONDARY_PROPERTIES_FILE);
			ContainerController cc = rt.createAgentContainer(p);
			
			AgentController acSimul = cc.createNewAgent("Environnement", "fr.shipsimulator.agent.EnvironnementAgent", null);
			acSimul.start();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
