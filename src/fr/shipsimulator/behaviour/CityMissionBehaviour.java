package fr.shipsimulator.behaviour;

import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;

public class CityMissionBehaviour extends TickerBehaviour {
	private static final long serialVersionUID = 1L;
	private City city;

	public CityMissionBehaviour(Agent a, long period) {
		super(a, period);
		city = ((CityAgent)this.myAgent).getCity();
	}
	
	protected void onTick() {
		Integer type = 0;
		Integer quantity = 1;
		city.getNeeds(type, quantity);
		// Envoie un message à l'agent mission pour signifier les besoins
		MainGui.writeLog("City Agent", "City needs " + quantity + " of " + type);
		ACLMessage request = new ACLMessage();
		request.setPerformative(ACLMessage.PROPOSE);
		request.addReceiver(new AID("Mission", AID.ISLOCALNAME));
		GenericMessageContent<AID> mcAid = new GenericMessageContent<AID>();
		mcAid.content.add(this.myAgent.getAID());
		GenericMessageContent<Integer> mcQuant = new GenericMessageContent<Integer>();
		mcQuant.content.add(type);
		mcQuant.content.add(quantity);
		request.setContent(mcAid.serialize() + "$:!" + mcQuant.serialize());
		myAgent.send(request);
	}

}
