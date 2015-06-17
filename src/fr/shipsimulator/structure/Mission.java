package fr.shipsimulator.structure;

public class Mission {
	
	private Integer id;
	private City departure;
	private City arrival;
	
	private Ressource ressource;
	private Integer resourceAmount;
	
	private static Integer idCounter = 0;
	
	public Mission(City departure, City arrival, Ressource ressource, Integer resourceAmount) {
		super();
		this.departure = departure;
		this.arrival = arrival;
		this.ressource = ressource;
		this.resourceAmount = resourceAmount;
		this.id = idCounter++;
	}
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public static void resetId(){
		idCounter = 0;
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
