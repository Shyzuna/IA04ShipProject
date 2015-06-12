package fr.shipsimulator.conteneur;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;

public class MainBoot {	
	public static String MAIN_PROPERTIES_FILE = "properties/mainbootConf.conf";
	
	public static void main(String[] args) {
		Runtime rt = Runtime.instance();
		Profile p = null;
		try {
			p = new ProfileImpl(MAIN_PROPERTIES_FILE);
			rt.createMainContainer(p);
		}
		catch(Exception ex) {
			System.out.println("Erreur mainContaineur : " + ex.getMessage());
		}
	}

}