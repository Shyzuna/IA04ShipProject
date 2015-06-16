package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.awt.Point;
import java.util.List;

import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.structure.GenericMessageContent;

public class ObserverObservBehaviour extends Behaviour implements Constante{
	private static final long serialVersionUID = 1L;

	private final Integer porteeObs;
	
	private Point currentposition;
	private int[][] vision;
	
	public ObserverObservBehaviour() {
		porteeObs = OBS_PORTEE;
		this.vision = new int[2 * porteeObs + 1][2 * porteeObs + 1];
		//this.currentposition.setLocation(departure.getPosX(), departure.getPosY());
	}
	
	@Override
	public void action() {
		MessageTemplate mt;
		ACLMessage msg;
		
		mt = new MessageTemplate(new SuroundingEnvironnementResponse());
		msg = myAgent.receive(mt);
		if (msg != null) {
			List<Integer> surrounding = new GenericMessageContent<Integer>().deserialize(msg.getContent());
			
			for(int i = 0; i < 2 * porteeObs + 1; i++){
				for(int j = 0; j < 2 * porteeObs + 1; j++){
					vision[i][j] = surrounding.get(2*i*porteeObs + j);
				}
			}
		}
		else block();		
	}

	@Override
	public boolean done() {
		return false;
	}
	
	private void askSuroundingEnvironnement(){
		ACLMessage envRequest = new ACLMessage(ACLMessage.REQUEST);

		envRequest.addReceiver(new AID("Environnement", AID.ISLOCALNAME));
		GenericMessageContent<Integer> pos = new GenericMessageContent<Integer>();
		pos.content.add((int) currentposition.getX());
		pos.content.add((int) currentposition.getY());
		pos.content.add(porteeObs);
		
		envRequest.setContent(pos.serialize());
		myAgent.send(envRequest);
	}
	
	private class SuroundingEnvironnementResponse implements MessageTemplate.MatchExpression {
		private static final long serialVersionUID = 1L;
		public boolean match(ACLMessage msg) {
	    	return  msg.getSender().getName().matches("Environnement(.*)") && msg.getPerformative() == ACLMessage.INFORM;
	    }
	}
}
