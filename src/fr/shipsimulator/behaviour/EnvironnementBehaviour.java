package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;
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
			if(sender.getName().matches("Observer(\\d)*")){
				if(msg.getPerformative() == ACLMessage.REQUEST && msg.getContent() != null){
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
					
					MessageContent messageContent = new MessageContent();
					//le message contient les ligne du tableau à la suite
					for(int i = 0; i < 2 * portee + 1; i++){
						for(int j = 0; j < 2 * portee + 1; j++){
							messageContent.content.add(vision[j][i]);
						}
					}
					
					ACLMessage reply = msg.createReply();
					reply.setContent(messageContent.serialize());
					ea.send(reply);
				}
			}
			//else if matches pour les autres agents...
			
		}	
	}
	
	@Override
	public boolean done() {
		return false;
	}

}
