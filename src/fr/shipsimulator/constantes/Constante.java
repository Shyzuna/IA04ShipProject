package fr.shipsimulator.constantes;

public interface Constante {
	
	//ACTION
	int QUIT = 5;
	
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