package fr.shipsimulator.structure;

public class Mission {
	private Integer id;
	public Integer getId() {
		return id;
	}

	private City departure;
	private City arrival;
	
	private Ressource ressource;
	private Integer resourceAmount;
	
	public Mission(City departure, City arrival, Ressource ressource, Integer resourceAmount) {
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
	
	public Ressource getRessource() {
		return ressource;
	}
	
	public int getResourceAmount() {
		return resourceAmount;
	}
	
	public boolean equals(Mission other){
		if (!departure.equals(other.getDeparture()))
			return false;
		if(!arrival.equals(other.getArrival()))
			return false;
		return (ressource == other.getRessource() && resourceAmount == other.getResourceAmount());
	}
}
