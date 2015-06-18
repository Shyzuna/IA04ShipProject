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
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;

public class CaptainDirectionBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
	
	private City departure;
	private City destination;
	private Point currentPosition;
	private Point askedPosition;
	
	private Point lastDirection;
	private Integer cptObsResponse;
	
	private int[][] vision;
	private Integer boatIndex;
		
	public CaptainDirectionBehaviour(BoatCaptainAgent ag) {
		super(ag);
		MainGui.writeLog(ag.getLocalName(), "New DirectionBehaviour");
		myAgent = ag;
		
		this.boatIndex = MAX_OBS_PORTEE;
		this.departure =  ((BoatCaptainAgent) myAgent).getCityDeparture();
		this.destination = ((BoatCaptainAgent) myAgent).getCurrentMission().getArrival();
		this.currentPosition = new Point();
		this.currentPosition.setLocation(ag.getBoat().getBoat().getPosX(), ag.getBoat().getBoat().getPosY());
		Integer offsetX = destination.getPosX() - departure.getPosX() == 0 ? 0 : (destination.getPosX() - departure.getPosX()) / Math.abs(destination.getPosX() - departure.getPosX());
		Integer offsetY = destination.getPosY() - departure.getPosY() == 0 ? 0 : (destination.getPosY() - departure.getPosY()) / Math.abs(destination.getPosY() - departure.getPosY());
		this.lastDirection = new Point(offsetX, offsetY);
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
				
				// Reset les observations pr�c�dentes
				vision = new int[2* MAX_OBS_PORTEE +1][2* MAX_OBS_PORTEE +1];
				
				askForObservation();
				state = State.WAIT_ALL_OBSERVATIONS;
			}
		}
		else if(state == State.WAIT_ALL_OBSERVATIONS){
			mt = new MessageTemplate(new ObservationResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println("nya7");
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
					determineDirection();
					sendDirection();
					state = State.DIRECTION_SENDED;
				}
				
			}
		}
		else if(state == State.DIRECTION_SENDED){
			mt = new MessageTemplate(new DirectionResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				if(msg.getPerformative() == ACLMessage.AGREE){
					// Mise a jour des vars locales pour le prochain tour
					currentPosition = askedPosition;
					
					lastDirection.x = askedPosition.x - currentPosition.x;
					lastDirection.y = askedPosition.y - currentPosition.y;
				}
				else{
					MainGui.writeLog("CaptainDirectionBehaviour", "Deplacement en x:" + askedPosition.x + ", y:" + askedPosition.y + " refus�e");
				}
				// Sinon on saute le tour
			}
		}
	}
	
	private void askForObservation(){
		ACLMessage obsRequest = new ACLMessage(ACLMessage.REQUEST);
		
		// Envoyer � tous les observer
		for (String s : crewMembers){
			System.out.println(s);
			if(s.matches("Observeur(.*)")){
				obsRequest.addReceiver(new AID(s,AID.ISLOCALNAME));
			}
		}
		
		// Mettre les coord de la position actuelle
		GenericMessageContent<Integer> pt = new GenericMessageContent<Integer>();
		
		pt.content.add(currentPosition.x);
		pt.content.add(currentPosition.y);
		obsRequest.setContent(ObserveRequestPatern + pt.serialize());
		myAgent.send(obsRequest);
	}
	
	private void determineDirection(){
		if(vision[boatIndex][boatIndex] != Constante.SELF) MainGui.writeLog("CaptainDirectionBehaviour", "Erreur de vision, mon bateau n'est pas au centre");
		
		askedPosition = new Point(-9, -9);
		// On essaye de garder le meme cap
		if(vision[boatIndex+lastDirection.x][boatIndex+lastDirection.y] == SEA){
			askedPosition.x = currentPosition.x + lastDirection.x;
			askedPosition.y = currentPosition.y + lastDirection.y;
		}
		// Sinon on essaye de se rapprocher des coordonn�es de la destination
		else{
			if(currentPosition.x < destination.getPosX() && vision[boatIndex+1][boatIndex] == SEA) askedPosition.x = currentPosition.x + 1;
			else if(vision[boatIndex-1][boatIndex] == SEA) askedPosition.x = currentPosition.x - 1;
			
			if(currentPosition.y < destination.getPosY() && vision[boatIndex][boatIndex+1] == SEA) askedPosition.y = currentPosition.y + 1;
			else if(vision[boatIndex][boatIndex-1] == SEA) askedPosition.y = currentPosition.y - 1;
		}
		// On n'a encore rien trouv�
		if(askedPosition.x == -9 || askedPosition.y == -9){
			for(int i = boatIndex-1; i <= boatIndex+1; i++){
				for(int j = boatIndex-1; j <= boatIndex+1; j++){
					if(vision[i][j] == SEA){
						askedPosition.x = currentPosition.x + i - boatIndex;
						askedPosition.y = currentPosition.y + i - boatIndex;
						 break;
					}
				}
			}
		}
		// Blocage, on ne bouge pas
		if(askedPosition.x == -9 || askedPosition.y == -9){
			askedPosition.x = currentPosition.x;;
			askedPosition.y = currentPosition.y;
		}
	}
	
	private void sendDirection(){
		ACLMessage moveRequest = new ACLMessage(ACLMessage.REQUEST);
		moveRequest.addReceiver(myBoat);
		
		// Mettre les coord de la position demandee
		GenericMessageContent<Integer> pt = new GenericMessageContent<Integer>();
		pt.content.add(askedPosition.x);
		pt.content.add(askedPosition.y);
		moveRequest.setContent(MovingRequestPatern + pt.serialize());
		myAgent.send(moveRequest);
	}
	
	// CADENCEUR
	public class CaptainTickerBehaviour extends TickerBehaviour implements Constante{
		private static final long serialVersionUID = 1L;

		public CaptainTickerBehaviour(Agent a, long period) {
			super(a, Constante.SIMULATION_TICK);
		}

		@Override
		protected void onTick() {
			//askForCrewMembers();
			state = State.OBS_LIST_ASKED;
		}	
	}
}
