package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.awt.Point;
import java.util.List;

import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.structure.GenericMessageContent;

public class ObserverObservBehaviour extends CrewMainBehaviour{
	private static final long serialVersionUID = 1L;

	private final Integer porteeObs;
	
	
	
	public ObserverObservBehaviour(BoatCrewAgent ag) {
		super(ag);
		porteeObs = OBS_PORTEE;
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		mt = new MessageTemplate(new ObserveRequest());
		msg = myAgent.receive(mt);
		if (msg != null) {
			List<Integer> pos = new GenericMessageContent<Integer>().deserialize(msg.getContent(),Integer.class);
			askSuroundingEnvironnement(new Point(pos.get(0), pos.get(1)));
		}
		else{
			mt = new MessageTemplate(new SuroundingEnvironnementResponse());
			msg = myAgent.receive(mt);
			if (msg != null) {
				System.out.println("nya5");
				transfertObservToCaptain(msg);
			}
			else block();
		}
	}
	
	private void askSuroundingEnvironnement(Point p){
		ACLMessage envRequest = new ACLMessage(ACLMessage.REQUEST);
		System.out.println("pointx:"+p.x+" pointy:"+p.y);
		envRequest.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
		GenericMessageContent<Integer> pos = new GenericMessageContent<Integer>();
		pos.content.add((int) p.getX());
		pos.content.add((int) p.getY());
		pos.content.add(porteeObs);
		
		envRequest.setContent(pos.serialize());
		myAgent.send(envRequest);
	}
	
	private void transfertObservToCaptain(ACLMessage msg){
		ACLMessage obsResponse = new ACLMessage(ACLMessage.INFORM);
		obsResponse.setContent(ObservationResponsePattern + msg.getContent());
		myAgent.send(obsResponse);
	}
}
