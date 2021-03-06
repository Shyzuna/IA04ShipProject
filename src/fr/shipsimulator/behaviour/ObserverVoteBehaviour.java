package fr.shipsimulator.behaviour;

import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.util.List;
import java.util.Random;

import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.Mission;


public class ObserverVoteBehaviour extends CrewMainBehaviour{
	public ObserverVoteBehaviour(BoatCrewAgent ag) {
		super(ag);
	}

	private static final long serialVersionUID = 1L;

	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		mt = new MessageTemplate(new MissionCrewRequest());
		msg = myAgent.receive(mt);
		if(msg != null){
			Mission missionChoosed = null;
			Integer MaxVote = null;
			Integer randValue;
			List<Mission> missionList = new GenericMessageContent<Mission>().deserialize(msg.getContent(), Mission.class);
			
			// Init seed différente
			Random r = new Random();
			for(Mission m : missionList){
				randValue = r.nextInt()*100;
				if(MaxVote == null || randValue > MaxVote){
					MaxVote = randValue;
					missionChoosed = m;
				}
			}
			
			ACLMessage reply = msg.createReply();
			reply.setPerformative(ACLMessage.INFORM);
			GenericMessageContent<Mission> missionReply = new GenericMessageContent<Mission>();
			missionReply.content.add(missionChoosed);
			
			reply.setContent(MissionCrewResponsePattern + missionReply.serialize());
			myAgent.send(reply);
		}
		else block();
	}
}
