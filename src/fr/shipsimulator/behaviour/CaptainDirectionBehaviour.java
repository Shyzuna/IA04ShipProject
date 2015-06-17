package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.agent.boatCrew.BoatCaptainAgent;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;

public class CaptainDirectionBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;
	
	
	
	private City departure;
	private City destination;
	
	private Direction lastDirection;
	private Integer cptObsResponse;
	
	public CaptainDirectionBehaviour(BoatCrewAgent ag) {
		super(ag);

		MainGui.writeLog("CaptainDirectionBehaviour", "New Behaviour");
		this.departure =  myAgent.getCityDeparture();	
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
		
		// Creer liste des missions		
		obsRequest.setContent("ObservRequest");
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
			askForObservation();		
		}	
	}
}
