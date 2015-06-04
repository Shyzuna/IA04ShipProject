package fr.shipsimulator.structure;

public class BoatCrew {
	public enum CrewType {OBSERVER, CAPTAIN, GUNNER}
	
	private CrewType crewType;
	
	public BoatCrew(CrewType type) {
		this.crewType = type;
	}
	

	public CrewType getType() {
		return crewType;
	}

	public void setType(CrewType type) {
		this.crewType = type;
	}
	
	// Stats, etc
	// TODO
}
