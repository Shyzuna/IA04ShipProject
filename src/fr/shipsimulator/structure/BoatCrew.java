package fr.shipsimulator.structure;

public class BoatCrew {
	public enum CrewType {
		OBSERVER,
		CAPTAIN,
		GUNNER
	}
	
	private CrewType type;
	private Boolean alive;

	public CrewType getType() {
		return type;
	}

	public void setType(CrewType type) {
		this.type = type;
	}

	public Boolean isAlive() {
		return alive;
	}

	public void kill() {
		alive = false;
	}
	
	// Stats, etc
	// TODO
}
