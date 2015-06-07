package fr.shipsimulator.constantes;

public interface Constante {
	
	//ACTION
	int QUIT = 0;
	int START = 1;
	int STOP = 2;
	int SUSPEND = 3;
	int RUNNING = 4;
	long SIMULATION_TICK = 1000;
	
	//INTERFACE
	int BORDER_H = 38;
	int BORDER_W = 12;
	int DEFAULT_FACTOR = 15;
	int MAX_W = 1920;
	int MAX_H = 1080;
	int MIN_W = 800;
	int MIN_H = 600;
	
	//MAP GRAPHICS
	int MAP_H = 600;
	int MAP_W = 800;
	
	String MAP_PATH = "./ressource/carte.png";
	String BATEAU_PATH = "./ressource/bateau_icone.png";
	String VILLE_PATH = "./ressource/ville_icone.png";

	//MAP DATA
	int DEFAULT_COLS = 30;
	int DEFAULT_ROWS = 30;
	int SEA = '0';
	int LAND = '1';
	int SHIP = '2';
	int CITY = '3';
	int SELF = '4';
	
	//BOAT DEFAULT DATA
	int BOAT_LIFE = 300;
	/*int BOAT_SPEED = 1; // la vitesse complique les choses */
	int DECK_CREW_NB = 1;
	int CREW_CAPACITY_PER_DECK = 5;
	int DECK_STORAGE_NB = 20;
	int STORAGE_CAPACITY_PER_DECK = 1;
	int DECK_ARMING_NB = 1;
	int CANNON_CAPACITY_PER_DECK = 10;
	int CANNON_NB = 0;
	int CANNON_POWER = 50;
	double TOUCH_FIRE_PROBABILITY = 0.1;
	double DECK_DESTRUCTION_PROBABILITY = 0.05;
	double CREW_DEATH_DECK_DESTRUCTION_PROBABILITY = 0.4;
	double CHAIN_EXPLOSION_PROBABILITY = 0.1;
	
}
