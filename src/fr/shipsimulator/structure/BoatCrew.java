package fr.shipsimulator.structure;

public class BoatCrew {
	public enum CrewType {
		OBSERVER,
		CAPTAIN,
		GUNNER
	}
	
	private CrewType type;

	public CrewType getType() {
		return type;
	}

	public void setType(CrewType type) {
		this.type = type;
	}
	
	// Stats, etc
	// TODO
}
