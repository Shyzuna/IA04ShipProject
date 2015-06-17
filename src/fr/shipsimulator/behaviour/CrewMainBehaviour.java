package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;

import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.structure.GenericMessageContent;

public abstract class CrewMainBehaviour extends Behaviour implements Constante{
	private static final long serialVersionUID = 1L;
	
	protected enum State {NO_MISSION, MISSION_LIST_ASKED, OBS_LIST_ASKED, WAIT_FOR_VOTE, WAIT_FOR_CONFIRM, MISSION_OK, 
							PAUSE, WAIT_ALL_OBSERVATIONS, DIRECTION_SENDED};
	
	protected static final String CrewListResponsePattern = "CrewListResponse:";
	
	protected static final String MissionListResponsePattern = "MissionListResponse:";
	protected static final String MissionCrewResponsePattern = "MissionCrewResponse:";
	protected static final String MissionConfirmResponsePattern = "MissionConfirmResponse:";
	protected static final String ObservationResponsePattern = "ObservationResponse:";
	protected static final String DirectionResponsePattern = "DirectionResponse:";
	
	protected static final String MissionlistRequestPatern = "MissionListRequest:";
	protected static final String MissionVoteRequestPatern = "MissionVoteRequest:";
	protected static final String ConfirmMissionVoteRequestPatern = "ConfirmMissionVoteRequest:";
	protected static final String ObserveRequestPatern = "ObserveRequest:";

	protected boolean done = false;
	protected State state;
	
	protected AID myBoat;
	protected List<AID> crewMembers;
	protected Integer nbCrew;
		
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
	
	protected void updateCrewMembers(String msg){
		crewMembers = new GenericMessageContent<AID>().deserialize(msg);
		nbCrew = crewMembers.size();
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
	
	protected class MissionCrewRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(MissionVoteRequestPatern + "(.*)") && msg.getPerformative() == ACLMessage.REQUEST){
				msg.setContent(msg.getContent().split(MissionVoteRequestPatern)[0]);
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
		
	// Observers
	protected class ObserveRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(ObserveRequestPatern + "(.*)") && msg.getPerformative() == ACLMessage.REQUEST){
				msg.setContent(msg.getContent().split(ObserveRequestPatern)[0]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class SuroundingEnvironnementResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return  msg.getSender().getName().matches("Environnement(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
}
