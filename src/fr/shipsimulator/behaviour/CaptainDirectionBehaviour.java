package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.awt.Point;
import java.util.List;

import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.behaviour.CrewMainBehaviour.SuroundingEnvironnementResponse;
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
	
	private int[][] vision;
	private Integer boatIndex;
		
	public CaptainDirectionBehaviour(BoatCrewAgent ag) {
		super(ag);
		MainGui.writeLog("CaptainDirectionBehaviour", "New Behaviour");
		myAgent = (BoatCaptainAgent) ag;
		this.boatIndex = MAX_OBS_PORTEE;
		this.departure =  ((BoatCaptainAgent) myAgent).getCityDeparture();
		this.destination = ((BoatCaptainAgent) myAgent).getCurrentMission().getArrival();
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
				
				// Reset les observations précédentes
				vision = new int[2* MAX_OBS_PORTEE +1][2* MAX_OBS_PORTEE +1];
				
				askForObservation();
				state = State.WAIT_ALL_OBSERVATIONS;
			}
		}
		else if(state == State.WAIT_ALL_OBSERVATIONS){
			mt = new MessageTemplate(new ObservationResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				mt = new MessageTemplate(new ObservationResponse());
				msg = myAgent.receive(mt);
				if (msg != null) {
					List<Integer> surrounding = new GenericMessageContent<Integer>().deserialize(msg.getContent());
					
					// Ofset pour centrer
					Integer porteObs = (int) Math.sqrt(surrounding.size());
					porteObs = (porteObs-1) / 2;
					
					Integer cptList = 0;
					for(int i = boatIndex-porteObs; i <= boatIndex + porteObs; i++){
						for(int j = boatIndex-porteObs; j <= boatIndex + porteObs; j++){
							vision[i][j] = surrounding.get(cptList);
							cptList++;
						}
					}
				}
				if(cptObsResponse >= nbCrew){
					sendDirection();
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

		// Envoyer à tous les observer
		for (AID aid : crewMembers) obsRequest.addReceiver(aid);
		
		// Mettre les coord de la position actuelle
		GenericMessageContent<Point> pt = new GenericMessageContent<Point>();
		pt.content.add(currentPosition);
		obsRequest.setContent(ObserveRequestPatern + pt.serialize());
		myAgent.send(obsRequest);
	}
	
	private void sendDirection(){
		if(vision[boatIndex][boatIndex] != Constante.SELF) MainGui.writeLog("CaptainDirectionBehaviour", "Erreur de vision, mon bateau n'est pas au centre");
		
		// TODO: Vision a +1 uniquement pour l'instant
		// En tenant compte du cap précédent, déterminer l'évitement si besoin
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
