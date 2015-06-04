package fr.shipsimulator.behaviour;

import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;

public class SendMapDataBehaviour extends CyclicBehaviour {

	private static final long serialVersionUID = 1L;
	private EnvironnementAgent ea;
	
	public  SendMapDataBehaviour() {
		ea = (EnvironnementAgent) this.myAgent;
	}
	
	
	
	@Override
	public void action(){
		/*MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		ACLMessage msg = receive(mt);
		if (msg != null) {
			ACLMessage reply = msg.createReply();
			//dans le message reçu il doit y avoir la position de l'agent observer et sa portée
			int posX = 0;
			int posY = 0;
			int portee = 1;
			int[][] vision = new int[2*portee+1][2*portee+1];
			for(int i = 0; i < portee; i++){
				for(int j = 0; j < portee; j++){
					if(i != 0 || j != 0){
						vision[portee + i][portee + j] = ea.getMapData()[posX + i][posY + j];
						vision[portee + i][portee - j] = ea.getMapData()[posX + i][posY - j];
						vision[portee - i][portee + j] = ea.getMapData()[posX - i][posY + j];
						vision[portee - i][portee - j] = ea.getMapData()[posX - i][posY - j];
					}
				}
			}
			vision[0][0] = Constante.SELF;
			//sérialiser vision et l'envoyer
			reply.setContent("");
			send(reply);
		}*/
		block();	
	}
	
}
