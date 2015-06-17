package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;

public abstract class CrewMainBehaviour extends Behaviour implements Constante{
	private static final long serialVersionUID = 1L;
	
	protected enum State {NO_MISSION, MISSION_LIST_ASKED, OBS_LIST_ASKED, WAIT_FOR_VOTE, WAIT_FOR_CONFIRM, MISSION_OK, 
							PAUSE, WAIT_ALL_OBSERVATIONS, DIRECTION_SENDED};
	
	protected static final String CrewListResponsePattern = "CrewListResponse";
	
	protected static final String MissionListResponsePattern = "MissionListResponse";
	protected static final String MissionCrewResponsePattern = "MissionCrewResponse";
	protected static final String MissionConfirmResponsePattern = "MissionConfirmResponse";
	protected static final String ObservationResponsePattern = "ObservationResponse";
	protected static final String DirectionResponsePattern = "DirectionResponse";
	protected static final String MissionCrewAskPattern = "DirectionResponse";

	protected boolean done = false;
	protected State state;
	
	protected AID myBoat;
	protected List<AID> crewMembers;
		
	public CrewMainBehaviour(BoatCrewAgent mAgent) {
		this.myBoat = mAgent.getMyBoat();
	}
	
	@Override
	public boolean done(){return done;}
	
	protected void askForCrewMembers(){
		ACLMessage memberRequest = new ACLMessage(ACLMessage.REQUEST);
		memberRequest.addReceiver(myBoat);
		memberRequest.setContent("CrewListRequest");
		myAgent.send(memberRequest);
	}	
	
	// ==== MESSAGE TEMPLATES ====
	protected class CrewListResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	if(msg.getContent().matches(CrewListResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(CrewListResponsePattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class MissionListResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	if(msg.getContent().matches(MissionListResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(MissionListResponsePattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class MissionCrewAsk implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(MissionCrewAskPattern + "(.*)") && msg.getPerformative() == ACLMessage.REQUEST){
				msg.setContent(msg.getContent().split(MissionCrewAskPattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
		
	protected class MissionCrewResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(MissionCrewResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(MissionCrewResponsePattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class MissionConfirmeResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	if(msg.getContent().matches(MissionConfirmResponsePattern + "(.*)")){
				msg.setContent(msg.getContent().split(MissionConfirmResponsePattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class ObservationResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(ObservationResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(ObservationResponsePattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class DirectionResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg){
	    	if(msg.getContent().matches(DirectionResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(DirectionResponsePattern)[0]);
				return true;
			}
	    	return false;
	    }
	}
}
