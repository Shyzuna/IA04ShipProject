package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.constantes.Constante.Direction;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;

public class CaptainDirectionBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
		
	private City departure;
	private City destination;
	
	private Direction lastDirection;
	private Integer cptObsResponse;
	
	public CaptainDirectionBehaviour(Agent a) {
		MainGui.writeLog("CaptainDirectionBehaviour", "New Behaviour");
		this.departure = ((BoatCaptainAgent) myAgent).getCityDeparture();	
		this.cptObsResponse = 0;
		
		askForCrewMembers();
		state = State.OBS_LIST_ASKED;
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.OBS_LIST_ASKED){
			
		}
		else if(state == State.WAIT_ALL_OBSERVATIONS){
			mt = new MessageTemplate(new ObservationResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				
				if(cptObsResponse >= crewMembers.size()){
					state = State.DIRECTION_SENDED;
				}
				
			}
		}
		else if(state == State.DIRECTION_SENDED){
			
		}
		
		// Recolter résultats observateurs
		
		//
	}

	@Override
	public boolean done() {
		return done;
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
