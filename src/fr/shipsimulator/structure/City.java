package fr.shipsimulator.structure;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import fr.shipsimulator.constantes.Constante;

public class City implements Constante {
	
	private int posX;
	private int posY;
	private Map<Integer, Integer> resources;
	
	public City(int x,int y){
		this.posX = x;
		this.posY = y;
		resources = new HashMap<Integer, Integer>();
		for (int i = 0; i < Ressource.values().length; ++i) {
			resources.put(i, new Random().nextInt((MAX_DEFAULT_RES - MIN_DEFAULT_RES) + 1) + MIN_DEFAULT_RES);
		}	
	}
	
	public Map<Integer, Integer> getResources() {
		return resources;
	}

	public void setResources(Map<Integer, Integer> resources) {
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

	public void getNeeds(Integer type, Integer quantity) {
		int minType = 0;
		int minQuant = -1;
		int maxQuant = 0;
		for (int i = 0; i < resources.size(); ++i) {
			int quant = resources.get(i);
			if (minQuant == -1) {
				minType = i;
				minQuant = quant;
				maxQuant = quant;
				continue;
			}
			if (quant < minQuant) {
				minType = i;
				minQuant = quant;
			}
			if (quant > maxQuant) {
				maxQuant = quant;
			}
		}
		int diff = maxQuant - minQuant;
		if (diff == 0) {
			diff = 1;
		}
		type = minType;
		quantity = diff;
	}
	
}
