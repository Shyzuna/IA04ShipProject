package fr.shipsimulator.structure;

import jade.core.AID;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.behaviour.CrewMemberDeathBehaviour;
import fr.shipsimulator.constantes.Constante;

public class Boat implements Constante {
	private Player boatOwner;
	private ArrayList<BoatCrewAgent> crewMembers;
	private Map<Integer, Integer> resources;
	private int posX;
	private int posY;
	private Boolean destroyed;
	
	// Boat "static" stats (but upgradable)
	private int maxLife;
	private int deckCrewNb;
	private int deckStorageNb;
	private int crewCapaciyPerDeck;
	private int storageCapacityPerDeck;
	private int deckArmingNb;
	private int cannonCapacityPerDeck;
	private int cannonNb;
	private int cannonPower;
	
	// Boat "moving" stats
	private int actualLife;
	private int actualDeckCrewNb;
	private int actualDeckStorageNb;
	private int actualDeckArmingNb;
	private int actualCannonNb;
	
	public Boat (Player boatOwner, int posX, int posY) {
		this.boatOwner = boatOwner;
		this.posX = posX;
		this.posY = posY;
		destroyed = false;
		resources = new HashMap<Integer, Integer>();
		crewMembers = new ArrayList<BoatCrewAgent>();
		initStats();
		restoreActualStats();
	}
	
	// Cannons
	
	public int fire(double powerBoostPercentage, double accuracyBoostPercentage, int impactCount) {
		// This function must be called each turns for each enemy present boat
		double probabilityOneCanonTouch = TOUCH_FIRE_PROBABILITY + TOUCH_FIRE_PROBABILITY * accuracyBoostPercentage;
		impactCount = 0;
		Integer usableCannonGunner = USABLECANNON_PER_GUNNER * getGunnerSize();
		Integer usableCannon = cannonNb < usableCannonGunner ? cannonNb : usableCannonGunner;
		for (int i = 0; i < usableCannon; ++i) {
			if (eventHappend(probabilityOneCanonTouch)) {
				++impactCount;
			}
		}
		return (int)(impactCount * (cannonPower + cannonPower * powerBoostPercentage));
	}
	
	// Mettre des messages l� dedans pour dire ce qui se passe aux joueurs ?
	public Boolean damage(int damageValue, int impactCount) {
		actualLife -= damageValue;
		if (actualLife < 0) {
			destroyed = true;
			return false;
		}
		for (int i = 0; i < impactCount; ++i) {
			if (tryDestroyDeckCrew() || tryDestroyDeckStorage() || tryDestroyDeckArming()) {
				destroyed = true;
				return false;
			}
		}
		return true;	
	}
	
	private Boolean tryDestroyDeckCrew() {
		if (eventHappend(DECK_DESTRUCTION_PROBABILITY)) {
			return destroyDeckCrew();
		}
		return true;
	}
	
	private Boolean tryDestroyDeckStorage() {
		if (eventHappend(DECK_DESTRUCTION_PROBABILITY)) {
			return destroyDeckStorage();
		}
		return true;
	}
	
	private Boolean tryDestroyDeckArming() {
		if (eventHappend(DECK_DESTRUCTION_PROBABILITY)) {
			return destroyDeckArming();
		}
		return true;
	}
	
	private Boolean destroyDeckCrew() {
		if (eventHappend(CREW_DEATH_DECK_DESTRUCTION_PROBABILITY)) {
			if (!killACrewMember()) {
				return false;
			}
		}
		int newMaxCrewMembers = deckCrewNb * crewCapaciyPerDeck;
		while (crewMembers.size() > newMaxCrewMembers) {
			if (!killACrewMember()) {
				return false;
			}
		}
		return true;
	}
	
	private Boolean destroyDeckStorage() {
		if (actualDeckStorageNb > 0) {
			--actualDeckStorageNb;
			int newMaxStorage = actualDeckStorageNb * storageCapacityPerDeck;
			while (resources.size() > newMaxStorage) {
				destroyAResource();
			}
		}
		return true;
	}
	
	private Boolean destroyDeckArming() {
		--actualDeckArmingNb;
		if (actualDeckArmingNb < 0) {
			actualDeckArmingNb = 0;
		}
		int newMaxCannons = actualDeckArmingNb * cannonCapacityPerDeck;
		if (actualCannonNb > newMaxCannons) {
			actualCannonNb = newMaxCannons;
		}
		if (eventHappend(CHAIN_EXPLOSION_PROBABILITY)){
			return false;
		}
		if (eventHappend(CREW_DEATH_DECK_DESTRUCTION_PROBABILITY)) {
			if (!killAGunner()) {
				return false;
			}
		}
		return true;
	}
	
	// Utils
	private Boolean eventHappend(double probability) {
		double randomProb = Math.random();
		if (randomProb < probability) {
			return true;
		}
		return false;
	}
	
	// Upgrades
	
	public void upgradeLife(int newValue) {
		actualLife += newValue - maxLife;
		maxLife = newValue;
	}
	
	public void upgradeDeckCrewNb(int newValue) {
		actualDeckCrewNb += newValue - deckCrewNb;
		deckCrewNb = newValue;
	}
	
	public void upgradeDeckStorageNb(int newValue) {
		actualDeckStorageNb += newValue - deckStorageNb;
		deckStorageNb = newValue;
	}

	public void upgradeCrewCapacityPerDeck(int newValue) {
		crewCapaciyPerDeck = newValue;
	}
	
	public void upgradeStorageCapacityPerDeck(int newValue) {
		storageCapacityPerDeck = newValue;
	}
	
	public void upgradeDeckArmingNb(int newValue) {
		actualDeckArmingNb += newValue - deckArmingNb;
		deckArmingNb = newValue;
	}
	
	public void upgradeCannonCapacityPerDeck(int newValue) {
		cannonCapacityPerDeck = newValue;
	}
	
	public void upgradeCannonNb(int newValue) {
		int maxCannons = actualDeckArmingNb * cannonCapacityPerDeck;
		if (newValue > maxCannons) {
			newValue = maxCannons;
		}
		actualCannonNb += newValue - cannonNb;
		cannonNb = newValue;
	}

	public void upgradeCannonPower(int newValue) {
		cannonPower = newValue;
	}
	
	// Accessors

	public int getDeckNb() {
		return deckCrewNb + deckStorageNb + deckArmingNb;
	}
	
	public int getDeckCrewNb() {
		return deckCrewNb;
	}

	public void setDeckCrewNb(int deckCrewNb) {
		this.deckCrewNb = deckCrewNb;
	}

	public Player getBoatOwner() {
		return boatOwner;
	}

	public void setBoatOwner(Player boatOwner) {
		this.boatOwner = boatOwner;
	}

	public int getDeckStorageNb() {
		return deckStorageNb;
	}

	public void setDeckStorageNb(int deckStorageNb) {
		this.deckStorageNb = deckStorageNb;
	}

	public int getCrewCapaciyPerDeck() {
		return crewCapaciyPerDeck;
	}

	public void setCrewCapaciyPerDeck(int crewCapaciyPerDeck) {
		this.crewCapaciyPerDeck = crewCapaciyPerDeck;
	}

	public int getStorageCapacityPerDeck() {
		return storageCapacityPerDeck;
	}

	public void setStorageCapacityPerDeck(int storageCapacityPerDeck) {
		this.storageCapacityPerDeck = storageCapacityPerDeck;
	}

	public int getDeckArmingNb() {
		return deckArmingNb;
	}

	public void setDeckArmingNb(int deckArmingNb) {
		this.deckArmingNb = deckArmingNb;
	}

	public int getCannonCapacityPerDeck() {
		return cannonCapacityPerDeck;
	}

	public void setCannonCapacityPerDeck(int cannonCapacityPerDeck) {
		this.cannonCapacityPerDeck = cannonCapacityPerDeck;
	}

	public int getCannonNb() {
		return cannonNb;
	}

	public void setCannonNb(int cannonNb) {
		this.cannonNb = cannonNb;
	}

	public int getCannonPower() {
		return cannonPower;
	}

	public void setCannonPower(int cannonPower) {
		this.cannonPower = cannonPower;
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

	public int getMaxLife() {
		return maxLife;
	}

	public void setMaxLife(int maxLife) {
		this.maxLife = maxLife;
	}

	public int getActualLife() {
		return actualLife;
	}

	public void setActualLife(int actualLife) {
		this.actualLife = actualLife;
	}

	public int getActualDeckCrewNb() {
		return actualDeckCrewNb;
	}

	public void setActualDeckCrewNb(int actualDeckCrewNb) {
		this.actualDeckCrewNb = actualDeckCrewNb;
	}

	public int getActualDeckStorageNb() {
		return actualDeckStorageNb;
	}

	public void setActualDeckStorageNb(int actualDeckStorageNb) {
		this.actualDeckStorageNb = actualDeckStorageNb;
	}

	public int getActualDeckArmingNb() {
		return actualDeckArmingNb;
	}

	public void setActualDeckArmingNb(int actualDeckArmingNb) {
		this.actualDeckArmingNb = actualDeckArmingNb;
	}

	public int getActualCannonNb() {
		return actualCannonNb;
	}

	public void setActualCannonNb(int actualCannonNb) {
		this.actualCannonNb = actualCannonNb;
	}
	
	public ArrayList<BoatCrewAgent> getCrewMembers() {
		return crewMembers;
	}

	public void setCrewMembers(ArrayList<BoatCrewAgent> crewMembers) {
		this.crewMembers = crewMembers;
	}
	
	public Boolean addCrewMember(BoatCrewAgent member) {
		if (crewMembers.size() >= actualDeckCrewNb * crewCapaciyPerDeck) {
			return false;
		}
		crewMembers.add(member);
		return true;
	}
	
	public Boolean killACrewMember() {
		if (crewMembers.size() <= 1) {
			return false;
		}
		int index = (int)(Math.random() * crewMembers.size());
		crewMembers.get(index).addBehaviour(new CrewMemberDeathBehaviour(crewMembers.get(index)));
		crewMembers.remove(index);
		return true;
	}
	
	public Boolean killAGunner() {
		for (int i = 0; i < crewMembers.size(); ++i) {
			if (crewMembers.get(i).getName().matches("(.*)BoatGunnerAgent(.*)")) {
				crewMembers.get(i).addBehaviour(new CrewMemberDeathBehaviour(crewMembers.get(i)));
				crewMembers.remove(i);
				break;
			}
		}
		if (crewMembers.size() == 0) {
			return false;
		}
		return true;
	}
	
	public Integer getGunnerSize() {
		Integer gunnerSize = 0;
		for (int i = 0; i < crewMembers.size(); ++i) {
			if (crewMembers.get(i).getName().matches("(.*)BoatGunnerAgent(.*)")) {
				++gunnerSize;
			}
		}
		return gunnerSize;
	}
	
	public void killAllCrewMembers() {
		for (int i = 0; i < crewMembers.size(); ++i) {
			crewMembers.get(i).addBehaviour(new CrewMemberDeathBehaviour(crewMembers.get(i)));
		}
		crewMembers.clear();
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
	
	public int getMaxStorage() {
		return actualDeckStorageNb * storageCapacityPerDeck;
	}
	
	public Boolean addResource(Integer quantity, Integer type) {
		if (resources.size() >= getMaxStorage() || type >= Ressource.values().length) {
			return false;
		}
		Integer actualQuant = resources.get(type);
		if (actualQuant + quantity < 0) {
			return false;
		}
		resources.put(type, actualQuant + quantity);
		return true;
	}
	
	public Boolean isDestroyed() {
		return destroyed;
	}
	
	public AID[] getCrewAIDs() {
		if (!crewMembers.isEmpty()) {
			AID aidList[] = new AID[crewMembers.size()];
			for (int i = 0; i < crewMembers.size(); ++i) {
				aidList[i] = crewMembers.get(i).getAID();
			}
			return aidList;
		}
		return null;
	}
	
	public AID getCaptainAID() {
		for (int i = 0; i < crewMembers.size(); ++i) {
			if (crewMembers.get(i).getName().matches("(.*)BoatCaptainAgent(.*)")) {
				return crewMembers.get(i).getAID();
			}		
		}
		return null;
	}
	
	// Init functions
	
	private void initStats() {
		maxLife = BOAT_LIFE;
		deckCrewNb = DECK_CREW_NB;
		deckStorageNb = CREW_CAPACITY_PER_DECK;
		crewCapaciyPerDeck = DECK_STORAGE_NB;
		storageCapacityPerDeck = STORAGE_CAPACITY_PER_DECK;
		deckArmingNb = DECK_ARMING_NB;
		cannonCapacityPerDeck = CANNON_CAPACITY_PER_DECK;
		cannonNb = CANNON_NB;
		cannonPower = CANNON_POWER;
	}
	
	private void restoreActualStats() {
		actualLife = maxLife;
		actualDeckCrewNb = deckCrewNb;
		actualDeckStorageNb = deckStorageNb;
		actualDeckArmingNb = deckArmingNb;
		actualCannonNb = cannonNb;
		resources = new HashMap<Integer, Integer>();
		for (int i = 0; i < Ressource.values().length; ++i) {
			resources.put(i, 0);
		}
		crewMembers = new ArrayList<BoatCrewAgent>();
	}
	
}
