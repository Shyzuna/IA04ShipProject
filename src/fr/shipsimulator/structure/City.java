package fr.shipsimulator.structure;

public class City {
	
	private int posX;
	private int posY;
	
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

	
	public City(int x,int y){
		this.posX = x;
		this.posY = y;
	}

	public boolean equals(City other) {
		return (posX == other.getPosX() && posY == other.getPosY());
	}
}
