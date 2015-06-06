package fr.shipsimulator.behaviour;

import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;

public class EnvironnementBehaviour extends Behaviour {

	private static final long serialVersionUID = 1L;
	private EnvironnementAgent ea;
	
	public EnvironnementBehaviour() {
		ea = (EnvironnementAgent) this.myAgent;
	}

	public void action() {
		ACLMessage msg = ea.receive();
		if(msg != null) {
			AID sender = msg.getSender();
			if(sender.getName().matches("Observer(\\d)*")){
				if(msg.getPerformative() == ACLMessage.REQUEST){
					//recu message du type posX posY portee
					int posX = 0;
					int posY = 0;
					int portee = 1;
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
					
					List<List<Integer>> visionList = new ArrayList<List<Integer>>();
					//chaque liste est une ligne du tableau et contient donc 2 * portee + 1 valeurs
					for(int i = 0; i < 2 * portee + 1; i++){
						visionList.add(new ArrayList<Integer>());
						for(int j = 0; j < 2 * portee + 1; j++){
							visionList.get(i).add(vision[j][i]);
						}
					}
					
					ObjectMapper mapper = new ObjectMapper();
					String serialized = "";
					try {
						serialized = mapper.writeValueAsString(visionList);
					} catch (JsonProcessingException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					ACLMessage reply = msg.createReply();
					reply.setContent(serialized);
					ea.send(reply);
				}
			}
			//else if matches pour les autres agents...
			
			//ea.getMainGui().update();
		}	
	}
	
	@Override
	public boolean done() {
		return false;
	}

}
