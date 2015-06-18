package fr.shipsimulator.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.shipsimulator.constantes.Constante;

public class City implements Constante {
	
	private int posX;
	private int posY;
	private HashMap<Integer, Integer> resources;
	
	public City(int x,int y){
		this.posX = x;
		this.posY = y;
		resources = new HashMap<Integer, Integer>();
		for (int i = 0; i < Ressource.values().length; ++i) {
			resources.put(i, new Random().nextInt((MAX_DEFAULT_RES - MIN_DEFAULT_RES) + 1) + MIN_DEFAULT_RES);
		}	
	}
	
	public City(){
	}
	
	public Map<Integer, Integer> getResources() {
		return resources;
	}

	public void setResources(HashMap<Integer, Integer> resources) {
		this.resources = resources;
	}
	
	public void destroyAResource() {
		if (resources.size() > 0) {
			int index = (int)(Math.random() * resources.size());
			resources.remove(index);
		}
	}
	
	public Boolean addResource(Integer quantity, Integer type) {
		if (type >= Ressource.values().length) {
			return false;
		}
		Integer actualQuant = resources.get(type);
		if (actualQuant + quantity < 0) {
			return false;
		}
		resources.put(type, actualQuant + quantity);
		return true;
	}
	
	public int getPosX() {
		return posX;
	}

	public void setPosX(int posX) {
		this.posX = posX;
	}

	public int getPosY() {
		return posY;
	}

	public void setPosY(int posY) {
		this.posY = posY;
	}

	public boolean equals(City other) {
		return (posX == other.getPosX() && posY == other.getPosY());
	}

	public int[] obtainNeeds() {
		int[] needs = new int[2];
		int minType = 0;
		int minQuant = -1;
		for (int i = 0; i < resources.size(); ++i) {
			int quant = resources.get(i);
			if (minQuant == -1) {
				minType = i;
				minQuant = quant;
				continue;
			}
			if (quant < minQuant) {
				minType = i;
				minQuant = quant;
			}
		}
		int min = minQuant < MIN_DEFAULT_RES ? MIN_DEFAULT_RES - minQuant : 1;
		int max = MAX_DEFAULT_RES - minQuant;
		needs[0] = minType;
		needs[1] = min + new Random().nextInt(max - min);
		return needs;
	}
	
}
