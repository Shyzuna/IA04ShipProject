package fr.shipsimulator.structure;

public class Mission {
	private City departure;
	private City arrival;
	
	private Resource ressource;
	private int resourceAmount;
	
	public Mission(City departure, City arrival, Resource ressource, int resourceAmount) {
		super();
		this.departure = departure;
		this.arrival = arrival;
		this.ressource = ressource;
		this.resourceAmount = resourceAmount;
	}

	public City getDeparture() {
		return departure;
	}
	
	public City getArrival() {
		return arrival;
	}
	
	public Resource getRessource() {
		return ressource;
	}
	
	public int getResourceAmount() {
		return resourceAmount;
	}
	
	
}
