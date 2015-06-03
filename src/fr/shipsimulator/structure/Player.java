package fr.shipsimulator.structure;

public class Player {
	private String playerName;
	private int money;
	
	public Player () {
		playerName = "defaultName";
		setMoney(0);
	}
	
	void setName(String name) {
		playerName = name;
	}
	
	String getName() {
		return playerName;
	}

	public int getMoney() {
		return money;
	}

	public void setMoney(int money) {
		this.money = money;
	}
	
}
