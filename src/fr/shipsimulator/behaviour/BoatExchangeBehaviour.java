package fr.shipsimulator.behaviour;

import java.util.List;

import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.Boat;
import fr.shipsimulator.structure.GenericMessageContent;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class BoatExchangeBehaviour extends CyclicBehaviour {
	private static final long serialVersionUID = 1L;
	private Boat boat;
	
	public BoatExchangeBehaviour (Agent a) {
		super(a);
		boat = ((BoatAgent)this.myAgent).getBoat();
	}
	
	public void action() {
		// Apr�s l'�change le capitaine informe le bateau des changements effectu�s dans la cargaison
		// Le capitaine envoie ExchangeInform s�parateur deux integer (type + quantit�), quantit� peut �tre n�gatif
		// On met ici � jour l'objet boat avec les changements de cargaison
		MessageTemplate mt = new MessageTemplate(new ExchangeInform());
		ACLMessage request = myAgent.receive(mt);
		if (request != null) {
			String [] split = request.getContent().split("\\$:!");
			if (split[1] != null) {
				List<Integer> reqExchange = new GenericMessageContent<Integer>().deserialize(split[1]);
				if (reqExchange.size() == 2) {
					if (!boat.addResource(reqExchange.get(1), reqExchange.get(0))) {
						try {
							throw new Exception("Captain do not respect shipment limits !");
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						MainGui.writeLog("BoatAgent", this.myAgent.getName() + " changed his reserves");
					}
				}
			}
		}
		block();
	}

	private class ExchangeInform implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getSender().getName().matches("(.*)BoatCaptainAgent(.*)") && msg.getContent().matches("ExchangeInform$:!(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
}