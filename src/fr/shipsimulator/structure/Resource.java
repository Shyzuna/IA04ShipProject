package fr.shipsimulator.structure;

public class Resource {
	public enum ResourceType { 
		WOOD,
		ROCK,
		IRON,
		FOOD
	}
	
	private ResourceType type;

	public ResourceType getType() {
		return type;
	}

	public void setType(ResourceType type) {
		this.type = type;
	}
	
	
}
