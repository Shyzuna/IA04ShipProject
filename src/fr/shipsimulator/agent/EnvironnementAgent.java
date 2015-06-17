package fr.shipsimulator.agent;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import fr.shipsimulator.behaviour.EnvironnementBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;
import fr.shipsimulator.structure.City;

/*
 * TODO : CHANGE DEFAULT COL/ROW to current and location of mapData init
 */
public class EnvironnementAgent extends GuiAgent implements Constante{
	private static final long serialVersionUID = 1L;
	
	private MainGui mainGui;
	private List<BoatAgent> listBoat;
	private List<CityAgent> listCity;
	private Integer stateSimulation;
	private int[][] mapData;
	private MissionAgent missionAgent;
	
	public List<CityAgent> getListCity() {
		return listCity;
	}

	public void setListCity(List<CityAgent> listCity) {
		this.listCity = listCity;
	}

	public Integer getStateSimulation() {
		return stateSimulation;
	}

	public void setStateSimulation(Integer stateSimulation) {
		this.stateSimulation = stateSimulation;
	}

	public List<BoatAgent> getListBoat() {
		return listBoat;
	}

	public void setListBoat(List<BoatAgent> listBoat) {
		this.listBoat = listBoat;
	}

	public int[][] getMapData() {
		return mapData;
	}

	public void setMapData(int col, int row, int data) {
		mapData[col][row] = data;
	}

	public MainGui getMainGui() {
		return mainGui;
	}

	public void setMainGui(MainGui mainGui) {
		this.mainGui = mainGui;
	}
	
	public MissionAgent getMissionAgent(){
		return missionAgent;
	}

	public EnvironnementAgent() {

	}

	@Override
	protected void setup() {
		//create and launch interface
		this.mainGui = new MainGui();
		MainGui.setMyAgent(this);
		new Thread(mainGui).start();
		
		AgentController agMission;
		missionAgent = new MissionAgent(this);
		try {
			agMission = this.getContainerController().acceptNewAgent("Mission", missionAgent);
			agMission.start();
		} catch (StaleProxyException e) {
			e.printStackTrace();
		}
		
		this.stateSimulation = Constante.STOP;
		this.listBoat = new ArrayList<>();
		this.listCity = new ArrayList<>();

		this.addBehaviour(new EnvironnementBehaviour(this));
	}
	
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		//handle Event from gui
		switch(ev.getType()){
			case QUIT:
				try {
					this.getContainerController().kill();
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
				break;
			case START:
				startSimulation();
				break;
			case STOP:
				stopSimulation();
				break;
			case SUSPEND:
				suspendSimulation();
				break;
		}
	}
	
	protected void fillMapData(String path){
		int[][] data = new int[MainGui.getCols()][MainGui.getRows()];
		Image map = null;

		System.out.println(this.mainGui);
		int caseHeight = MainGui.getFactor_grid_h();
		int caseWidth = MainGui.getFactor_grid_w();

		map = new Image(new File(MAP_PATH).toURI().toString(),caseWidth*MainGui.getCols(),caseHeight*MainGui.getRows(),false,false);
		PixelReader pr = map.getPixelReader();
		
		for(int row = 0; row < MainGui.getRows(); row++){
			for(int col = 0; col < MainGui.getCols(); col++){
				//checker dans la case correspondante qu'un max de pixels est bleu
				int bluePixels = 0;
				
				for(int i = row * caseHeight; i < (row + 1) * caseHeight; i++){
					for(int j = col * caseWidth; j < (col + 1) * caseWidth; j++){
						int rgb = pr.getArgb(j, i);
						int red = (rgb >> 16) & 0xFF;
						int green = (rgb >> 8) & 0xFF;
						int blue = rgb & 0xFF;
						if(blue > green && blue > red){
							bluePixels++;
						}
					}
				}
				if((float)bluePixels / (caseHeight * caseWidth) > 0.75)
					data[col][row] = SEA;
				else
					data[col][row] = LAND;
			}
		}
		this.mapData = data;
		
		//printDataMap();
		
	}
	
	public void printDataMap(){
	//affichage pour v�rification
			for(int i = 0; i < MainGui.getRows(); i++){
				for(int j = 0; j < MainGui.getCols(); j++){
					if(this.mapData[j][i] == LAND)
						System.out.print("# ");
					else if(this.mapData[j][i] == SEA)
						System.out.print(". ");
					else if(this.mapData[j][i] == CITY)
						System.out.print("C ");
					else
						System.out.print("B ");
				}
				System.out.println("");
			}
	}
	
	private boolean isBoatAt(int x, int y){
		for(BoatAgent ba : this.listBoat){
			if(ba.getBoat().getPosX() == x && ba.getBoat().getPosY() == y)
				return true;
		}
		return false;
	}
	
	private boolean cityCanBeAt(int x, int y){
		if(mapData[x][y] != Constante.LAND)
			return false;
		for(int i = -1; i <= 1; i++){
			if(i != 0 && ((x + i >= 0 && x + i < MainGui.getCols() && mapData[x + i][y] == Constante.SEA)
					|| (y + i >= 0 && y + i < MainGui.getRows() && mapData[x][y + i] == Constante.SEA)))
				return true;
		}
		return false;
	}
	
	public CityAgent getCityAgentByName(String name){
		for(int i = 0; i < listCity.size(); i++){
			if(listCity.get(i).getLocalName().equals(name))
				return listCity.get(i);
		}
		return null;
	}
	
	private void suspendSimulation(){
		if(this.stateSimulation == RUNNING){
			MainGui.writeLog("Env", "Suspend Simulation");
			this.stateSimulation = Constante.SUSPEND;
			for(BoatAgent ba : listBoat){
				ba.suspendBoat(); //suspend crewmember
				ba.doSuspend();
			}
			for(CityAgent ca : listCity){
				ca.doSuspend();
			}
		}
	}
	
	private void stopSimulation(){
		if(this.stateSimulation == RUNNING || this.stateSimulation == SUSPEND){
			MainGui.writeLog("Env", "Stop Simulation");
			this.stateSimulation = STOP;
			for(BoatAgent ba : listBoat){
				ba.stopBoat(); //delete crewmember
				ba.doDelete();
			}
			this.listBoat.clear();
			for(CityAgent ca : listCity){
				ca.doDelete();
			}
			this.listCity.clear();
			missionAgent.resetMissions();
		}
	}
	
	private void startSimulation(){
		if(this.stateSimulation == STOP){
			this.stateSimulation = RUNNING;
			MainGui.clearLog();
			MainGui.writeLog("Env", "Start Simulation");
			fillMapData(MAP_PATH);
			int x,y;
			Random rand = new Random();
			for(int i = 0; i < MainGui.getCity(); i++){
				do{
					x = rand.nextInt(MainGui.getCols()-1);
					y = rand.nextInt(MainGui.getRows()-1);
				}while(!cityCanBeAt(x, y));
				AgentController agCity;
				mapData[x][y] = Constante.CITY;
				CityAgent ca = new CityAgent(x,y);
				try {
					agCity = this.getContainerController().acceptNewAgent("City"+i, ca);
					this.listCity.add(ca);
					agCity.start();
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			}
			
			for(int i = 0; i < MainGui.getBoat();i++){
				City nextTo = listCity.get(i).getCity();
				x = -1;
				y = -1;
				if(nextTo.getPosX() + 1 < MainGui.getCols()
						&& mapData[nextTo.getPosX() + 1][nextTo.getPosY()] == Constante.SEA){
					x = nextTo.getPosX() + 1;
					y = nextTo.getPosY();
				}
				else if(nextTo.getPosX() - 1 >= 0
						&& mapData[nextTo.getPosX() - 1][nextTo.getPosY()] == Constante.SEA){
					x = nextTo.getPosX() - 1;
					y = nextTo.getPosY();
				}
				else if(nextTo.getPosY() + 1 < MainGui.getRows()
						&& mapData[nextTo.getPosX()][nextTo.getPosY() + 1] == Constante.SEA){
					x = nextTo.getPosX();
					y = nextTo.getPosY() + 1;
				}
				else if(nextTo.getPosY() - 1 >= 0
						&& mapData[nextTo.getPosX()][nextTo.getPosY() - 1] == Constante.SEA){
					x = nextTo.getPosX();
					y = nextTo.getPosY() - 1;
				}
				AgentController agBoat;
				BoatAgent ba = new BoatAgent(x,y, this);
				mapData[x][y] = Constante.SHIP;
				try {
					agBoat = this.getContainerController().acceptNewAgent("Boat"+i, ba);
					this.listBoat.add(ba);
					agBoat.start();
				} catch (StaleProxyException e) {
					e.printStackTrace();
				}
			}
			
		} else if(this.stateSimulation == SUSPEND){
			MainGui.writeLog("Env", "ReStart Simulation");
			this.stateSimulation = RUNNING;
			for(BoatAgent ba : listBoat){
				ba.activateBoat(); //activate crewmember
				ba.doActivate();
			}
			for(CityAgent ca : listCity){
				ca.doActivate();
			}
		}
	}
}
