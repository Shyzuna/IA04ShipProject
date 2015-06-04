package fr.shipsimulator.structure;

public class BoatCrew {
	public enum CrewType {OBSERVER, CAPTAIN, GUNNER}
	
	private CrewType crewType;
	private Boolean alive;
	
	public BoatCrew(CrewType type) {
		this.crewType = type;
	}	

	public CrewType getType() {
		return crewType;
	}

	public void setType(CrewType type) {
		this.crewType = type;
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
