package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.awt.Point;

import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;

public class CaptainDirectionBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
	
	private City departure;
	private City destination;
	private Point currentPosition;
	
	private Direction lastDirection;
	private Integer cptObsResponse;

	private BoatCaptainAgent myAgent;
	
	public CaptainDirectionBehaviour(BoatCrewAgent ag) {
		super(ag);
		MainGui.writeLog("CaptainDirectionBehaviour", "New Behaviour");
		myAgent = (BoatCaptainAgent) ag;
		this.departure =  myAgent.getCityDeparture();
		this.destination = myAgent.getCurrentMission().getArrival();
		this.currentPosition.setLocation(departure.getPosX(), departure.getPosY());
		this.lastDirection = Direction.NONE;
		this.cptObsResponse = 0;
		
		askForCrewMembers();
		state = State.OBS_LIST_ASKED;
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		if(state == State.OBS_LIST_ASKED){
			mt = new MessageTemplate(new CrewListResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				updateCrewMembers(msg.getContent());
				askForObservation();
				state = State.WAIT_ALL_OBSERVATIONS;
			}
		}
		else if(state == State.WAIT_ALL_OBSERVATIONS){
			mt = new MessageTemplate(new ObservationResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				
				if(cptObsResponse >= nbCrew){
					state = State.DIRECTION_SENDED;
				}
				
			}
		}
		else if(state == State.DIRECTION_SENDED){
			mt = new MessageTemplate(new DirectionResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				
			}
		}
	}
	
	private void askForObservation(){
		ACLMessage obsRequest = new ACLMessage(ACLMessage.REQUEST);

		// Envoyer � tous les observer
		for (AID aid : crewMembers) obsRequest.addReceiver(aid);
		
		// Mettre les coord de la position actuelle
		GenericMessageContent<Point> pt = new GenericMessageContent<Point>();
		pt.content.add(currentPosition);
		obsRequest.setContent(ObserveRequestPatern + pt.serialize());
		myAgent.send(obsRequest);
	}
	
	
	// CADENCEUR
	public class CaptainTickerBehaviour extends TickerBehaviour implements Constante{
		private static final long serialVersionUID = 1L;

		public CaptainTickerBehaviour(Agent a, long period) {
			super(a, Constante.SIMULATION_TICK);
		}

		@Override
		protected void onTick() {
			askForCrewMembers();
		}	
	}
}
