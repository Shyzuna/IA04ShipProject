package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.agent.MissionAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.MessageContent;
import fr.shipsimulator.structure.Mission;
import fr.shipsimulator.structure.Ressource;

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
			if(sender.getLocalName().matches("City(\\d)*")){
				if(msg.getPerformative() == ACLMessage.PROPOSE && msg.getContent() != null){
					List<Integer> recu = MessageContent.deserialize(msg.getContent());
					ACLMessage reply = msg.createReply();
					Random rand = new Random();
					List<CityAgent> cities = ma.getEnvironnementAgent().getListCity();
					City arrival =  ma.getEnvironnementAgent().getCityAgentByName("City"+recu.get(0)).getCity(); 
					if(arrival == null){
						reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
					}
					else {
						City departure = cities.get(rand.nextInt(cities.size())).getCity();
						while(departure.equals(arrival)) {
							departure = cities.get(rand.nextInt(cities.size())).getCity();
						}
						if (isPossibleMission(departure.getResources(), recu.get(0), recu.get(1))) {
							Ressource res = Ressource.WOOD;
							for (Ressource r : Ressource.values()) {
								if(r.ordinal() == recu.get(1)){
									res = r;
									break;
								}
							}
							Mission m = new Mission(departure, arrival, res, recu.get(2));
							boolean missionAlreadyExists = false;
							for(int i = 0; i < ma.getMissions().size(); i++){
								if(ma.getMissions().get(i).equals(m)) {
									missionAlreadyExists = true;
									break;
								}
							}
							if(missionAlreadyExists){
								reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
							}
							else {
								ma.addMission(m);
								MainGui.writeLog("Mission", "Nouvelle mission ajoutée : " + recu.get(2) + " " + res.name());
								reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
							}
						} else {
							reply.setPerformative(ACLMessage.REJECT_PROPOSAL);
						}
					}
					ma.send(reply);
				}
			}
			else if (sender.getLocalName().matches("Capitaine_Boat(\\d)*")){
				List<Mission> missions = ma.getMissions();
				if(msg.getPerformative() == ACLMessage.REQUEST && msg.getContent() != null){
					List<Integer> coord = new GenericMessageContent<Integer>().deserialize(msg.getContent());
					GenericMessageContent<Mission> result = new GenericMessageContent<Mission>();
					Iterator<Mission> iterMi = missions.iterator();
					while (iterMi.hasNext()) {
						if(iterMi.next().getDeparture().getPosX() == coord.get(0) && iterMi.next().getDeparture().getPosY() == coord.get(1)){
							if (isPossibleMission(iterMi.next().getDeparture().getResources(), iterMi.next().getRessource().ordinal(), iterMi.next().getResourceAmount())) {
								result.content.add(iterMi.next());
							} else {
								iterMi.remove();
							}
						}
					}
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent("MissionListResponse:" + result.serialize());
					ma.send(reply);
				}
				else if(msg.getPerformative() == ACLMessage.CONFIRM && msg.getContent() != null){
					//le message contient la mission choisie
					Mission chosen = new GenericMessageContent<Mission>().deserialize(msg.getContent(), Mission.class).get(0);
					boolean stillAvailable = false;
					Iterator<Mission> iterMi = missions.iterator();
					while (iterMi.hasNext()) {
						if(iterMi.next().equals(chosen)){
							stillAvailable = true;
							iterMi.remove();
							break;
						}
					}
					ACLMessage reply = msg.createReply();
					reply.setPerformative(stillAvailable ? ACLMessage.AGREE : ACLMessage.REFUSE);
					reply.setContent("MissionConfirmResponse:");

					ma.send(reply);
				}
			}
		}
	}

	private boolean isPossibleMission(Map<Integer, Integer> resources,
			Integer type, Integer quantity) {
		if (resources.get(type) < quantity) {
			return false;
		}
		return true;
	}

	@Override
	public boolean done() {
		return false;
	}

}
