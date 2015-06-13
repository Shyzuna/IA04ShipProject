package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;

public class CaptainDirectionBehaviour extends Behaviour implements Constante{
	private static final long serialVersionUID = 1L;


	private enum Direction{N, NE, E, SE, S, SO, O, NO};
	private enum State {WAIT_ALL_RESPONSES, DIRECTION_CHOICE, DIRECTION_SENDED};
	
	private State state;
	private boolean done = false;
	
	
	private City departure;
	private City destination;
	
	private Direction lastDirection;
	
	public CaptainDirectionBehaviour(Agent a, City departure) {
		this.departure = departure;	
		MainGui.writeLog("CaptainDirectionBehaviour", "New Behaviour");
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.WAIT_ALL_RESPONSES){
			mt = new MessageTemplate(new ObservationResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				
			}
		}
		else if(state == State.DIRECTION_CHOICE){
			
		}
		// Recolter résultats observateurs
		
		//
	}

	@Override
	public boolean done() {
		return done;
	}
	
	private void askForCrewMembers(){
		ACLMessage memberRequest = new ACLMessage(ACLMessage.REQUEST);
		memberRequest.addReceiver(((BoatCaptainAgent) myAgent).getMyBoat());
		memberRequest.setContent("CrewListRequest");
		myAgent.send(memberRequest);
	}
	
	private void askVoteToCrew(List<AID> crewMembers){		
		ACLMessage crewRequest = new ACLMessage(ACLMessage.REQUEST);
		
		// Envoyer à tous les observer
		for (AID aid : crewMembers) {
			crewRequest.addReceiver(aid);
		}
		
		// Creer liste des missions		
		crewRequest.setContent("ObservRequest");
		myAgent.send(crewRequest);
	}
	
	private void askForObservation(){
		
	}
	
	private class ObservationResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return msg.getContent().matches("ObservationResponse:(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
	
	// CADENCEUR
	public class CaptainTickerBehaviour extends TickerBehaviour implements Constante{
		private static final long serialVersionUID = 1L;

		public CaptainTickerBehaviour(Agent a, long period) {
			super(a, Constante.SIMULATION_TICK);
		}

		@Override
		protected void onTick() {
			askForObservation();		
		}	
	}
}
