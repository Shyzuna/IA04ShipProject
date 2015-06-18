package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;

public class CityMissionBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private City city;

	public CityMissionBehaviour(Agent a, long period) {
		super(a, period);
		city = ((CityAgent)this.myAgent).getCity();
		onTick();
	}
	
	protected void onTick() {
		int[] needs = city.obtainNeeds();
		Integer type = needs[0];
		Integer quantity = needs[1];
		// Envoie un message � l'agent mission pour signifier les besoins
		MainGui.writeLog("City Agent", this.myAgent.getLocalName() + " needs " + quantity + " of " + type);
		ACLMessage request = new ACLMessage(ACLMessage.PROPOSE);
		request.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		GenericMessageContent<Integer> mcQuant = new GenericMessageContent<Integer>();
		int cityID = Integer.parseInt(myAgent.getLocalName().replaceFirst("City", ""));
		mcQuant.content.add(cityID);
		mcQuant.content.add(type);
		mcQuant.content.add(quantity);
		request.setContent(mcQuant.serialize());
		myAgent.send(request);
	}

}
