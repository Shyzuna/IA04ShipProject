package fr.shipsimulator.constantes;

public interface Constante {
	
	//ACTION
	int QUIT = 0;
	int START = 1;
	int STOP = 2;
	int SUSPEND = 3;
	
	//INTERFACE
	int BORDER_H = 38;
	int BORDER_W = 12;
	int DEFAULT_FACTOR = 15;
	int MIN_FACTOR = 10;
	int MAX_FACTOR = 20;
	
	//MAP GRAPHICS
	int MAP_H = 600;
	int MAP_W = 800;
	
	String MAP_PATH = "./ressource/carte.png";
	String BATEAU_PATH = "./ressource/bateau_icone.png";

	//MAP DATA
	int DEFAULT_COLS = 30;
	int DEFAULT_ROWS = 30;
	int SEA = '0';
	int LAND = '1';
}
