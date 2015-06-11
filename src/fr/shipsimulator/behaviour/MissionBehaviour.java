package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.List;

import fr.shipsimulator.agent.MissionAgent;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.MessageContent;
import fr.shipsimulator.structure.Mission;

public class MissionBehaviour extends Behaviour {
	
	private static final long serialVersionUID = 1L;
	private MissionAgent ma;
	
	public MissionBehaviour(MissionAgent miss) {
		ma = miss;
	}

	@Override
	public void action() {
		ACLMessage msg = ma.receive();
		if(msg != null) {
			AID sender = msg.getSender();
			if(sender.getName().matches("City(\\d)*")){
				if(msg.getPerformative() == ACLMessage.PROPOSE && msg.getContent() != null){
					List<Integer> recu = MessageContent.deserialize(msg.getContent());
					/*Random rand = new Random();
					List<CityAgent> cities = ma.getEnvironnementAgent().getListCity();
					City arrival =  sender.getCity(); //comment trouver cette info ?
					City departure = cities.get(rand.nextInt(cities.size())).getCity();
					Mission m = new Mission(departure, arrival, recu.get(0), recu.get(1));
					ma.addMission(m);*/
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
					ma.send(reply);
				}
			}
			else if (sender.getName().matches("BoatCaptain(\\d)*")){
				List<Mission> missions = ma.getMissions();
				GenericMessageContent gmc= new GenericMessageContent();
				if(msg.getPerformative() == ACLMessage.REQUEST && msg.getContent() != null){
					//le message contient le nom de la ville dont on veut les missions
					for(int i = 0; i < missions.size(); i++){
						//si missions.get(i).getDeparture() est la même ville alors :
						gmc.content.add(missions.get(i));
					}
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(gmc.serialize());
					ma.send(reply);
				}
				else if(msg.getPerformative() == ACLMessage.INFORM && msg.getContent() != null){
					//le message contient la mission choisie
					Mission chosen = (Mission)gmc.deserialize(msg.getContent()).get(0);
					boolean stillAvailable = false;
					for(int i = 0; i < missions.size(); i++){
						if(missions.get(i).equals(chosen)){
							stillAvailable = true;
							break;
						}
					}
					ACLMessage reply = msg.createReply();
					reply.setPerformative(stillAvailable ? ACLMessage.AGREE : ACLMessage.REFUSE);
					reply.setContent(gmc.serialize());
					ma.send(reply);
				}
			}
		}
	}

	@Override
	public boolean done() {
		return false;
	}

}
