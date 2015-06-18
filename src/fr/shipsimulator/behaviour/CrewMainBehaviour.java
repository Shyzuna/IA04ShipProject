package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
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
							
	protected boolean done = false;
	protected State state;
	
	protected AID environnementAgent;
	protected AID missionAgent;
	protected AID myBoat;
	protected List<AID> crewMembers;
	protected Integer nbCrew;
		
	public CrewMainBehaviour(BoatCrewAgent mAgent) {
		this.myBoat = mAgent.getMyBoat();
		environnementAgent = getEnvironnementAgent(mAgent);
		missionAgent = getMissionAgent(mAgent);
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
		crewMembers = new GenericMessageContent<AID>().deserialize(msg, AID.class);
		nbCrew = crewMembers.size();
	}
	
	private AID getEnvironnementAgent(BoatCrewAgent selfAg) {
		AID rec = null;
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Environnement");
		sd.setName("Environnement");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(selfAg, template);
			rec = result[0].getName();
		} catch(FIPAException fe) {}
		return rec;
	}
	
	private AID getMissionAgent(BoatCrewAgent selfAg) {
		AID rec = null;
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Mission");
		sd.setName("Mission");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(selfAg, template);
			rec = result[0].getName();
		} catch(FIPAException fe) {}
		return rec;
	}
	
	// ==== MESSAGE TEMPLATES ====
	protected class CrewListResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	if(msg.getContent().matches(CrewListResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(CrewListResponsePattern)[1]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class MissionListResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	if(msg.getContent().matches(MissionListResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
	    		msg.setContent(msg.getContent().split(MissionListResponsePattern)[1]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class MissionCrewRequest implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(MissionVoteRequestPatern + "(.*)") && msg.getPerformative() == ACLMessage.REQUEST){
				msg.setContent(msg.getContent().split(MissionVoteRequestPatern)[1]);
				return true;
			}
	    	return false;
	    }
	}
		
	protected class MissionCrewResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(MissionCrewResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(MissionCrewResponsePattern)[1]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class MissionConfirmeResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	if(msg.getContent().matches(MissionConfirmResponsePattern + "(.*)")){
				msg.setContent(msg.getContent().split(MissionConfirmResponsePattern)[1]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class ObservationResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
			if(msg.getContent().matches(ObservationResponsePattern + "(.*)") && msg.getPerformative() == ACLMessage.INFORM){
				msg.setContent(msg.getContent().split(ObservationResponsePattern)[1]);
				return true;
			}
	    	return false;
	    }
	}
	
	protected class DirectionResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg){
	    	if(msg.getContent().matches(DirectionResponsePattern + "(.*)")){
				msg.setContent(msg.getContent().split(DirectionResponsePattern)[1]);
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
				msg.setContent(msg.getContent().split(ObserveRequestPatern)[1]);
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
