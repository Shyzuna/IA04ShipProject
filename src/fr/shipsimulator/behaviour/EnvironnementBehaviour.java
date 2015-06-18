package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.structure.GenericMessageContent;
import fr.shipsimulator.structure.MessageContent;

public class EnvironnementBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	private EnvironnementAgent ea;
	
	public EnvironnementBehaviour(EnvironnementAgent env) {
		ea = env;
	}

	public void action() {
		ACLMessage msg = ea.receive();
		if(msg != null) {
			AID sender = msg.getSender();
			if(sender.getLocalName().matches("Observeur(.*)")){
				if(msg.getPerformative() == ACLMessage.REQUEST && msg.getContent() != null){
					System.out.println("nya2");
					List<Integer> recu = MessageContent.deserialize(msg.getContent());
					int posX = recu.get(0);
					int posY = recu.get(1);
					int portee = recu.get(2);
					int[][] vision = new int[2 * portee + 1][2 * portee + 1];
					for(int i = 0; i <= portee ; i++){
						for(int j = 0; j <= portee ; j++){
							if(i == 0 && j == 0){
								vision[i][j] = Constante.SELF;
							}
							else {
								if(posX + i < ea.getMainGui().getCols()){
									vision[portee + i][portee + j] = 
											posY + j < ea.getMainGui().getRows() ? ea.getMapData()[posX + i][posY + j] : Constante.LAND;
									vision[portee + i][portee - j] = 
											posY - j >= 0 ? ea.getMapData()[posX + i][posY - j] : Constante.LAND;
								}
								if(posX - i >= 0){
									vision[portee - i][portee + j] = 
											posY + j < ea.getMainGui().getRows() ? ea.getMapData()[posX - i][posY + j] : Constante.LAND;
									vision[portee - i][portee - j] = 
											posY - j >= 0 ? ea.getMapData()[posX - i][posY - j] : Constante.LAND;
								}
							}
						}
					}
					
					GenericMessageContent<Integer> messageContent = new GenericMessageContent<Integer>();
					//le message contient les ligne du tableau à la suite
					for(int i = 0; i < 2 * portee + 1; i++){
						for(int j = 0; j < 2 * portee + 1; j++){
							messageContent.content.add(vision[j][i]);
						}
					}
					
					ACLMessage reply = msg.createReply();
					reply.setPerformative(ACLMessage.INFORM);
					reply.setContent(messageContent.serialize());
					ea.send(reply);
				}
			}
			else if(sender.getLocalName().matches("Boat(\\d)*")){
				if(msg.getPerformative() == ACLMessage.REQUEST && msg.getContent() != null){
					List<Integer> recu = MessageContent.deserialize(msg.getContent().split("\\$:!")[1]);
					int oldPosX = recu.get(0);
					int oldPosY = recu.get(1);
					int newPosX = recu.get(2);
					int newPosY = recu.get(3);
					ACLMessage reply = msg.createReply();
					if(ea.getMapData()[newPosX][newPosY] != Constante.SEA){
						reply.setPerformative(ACLMessage.REFUSE);
						reply.setContent("MovingResponse");
						ea.send(reply);
					}
					else {
						MessageContent mc = new MessageContent();
						mc.content = new ArrayList<Integer>();
						mc.content.add(newPosX);
						mc.content.add(newPosY);
						reply.setPerformative(ACLMessage.AGREE);
						reply.setContent("MovingResponse$:!"+mc.serialize());
						ea.send(reply);
						ea.setMapData(oldPosX, oldPosY, Constante.SEA);
						ea.setMapData(newPosX, newPosY, Constante.SHIP);
					}
				}
			}
		}	
	}
	
	@Override
	public boolean done() {
		return false;
	}

}
