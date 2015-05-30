package fr.shipsimulator.agent;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;
import fr.shipsimulator.behaviour.EnvironnementBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.MainGui;

public class EnvironnementAgent extends GuiAgent implements Constante{
	
	protected MainGui mainGui;

	public MainGui getMainGui() {
		return mainGui;
	}

	public void setMainGui(MainGui mainGui) {
		this.mainGui = mainGui;
	}

	public EnvironnementAgent() {

	}

	protected int[][] mapData;
	

	@Override
	protected void setup() {
		//create and launch interface
		mainGui = new MainGui();
		MainGui.setMyAgent(this);
		new Thread(mainGui).start();
		
		fillMapData(Constante.MAP_PATH);
		
		this.addBehaviour(new EnvironnementBehaviour());
	}
	
	@Override
	protected void onGuiEvent(GuiEvent ev) {
		//handle Event from gui
		switch(ev.getType()){
			case 1:
				System.out.println("nya");
		}
	}
	
	protected void fillMapData(String path){
		int[][] data = new int[DEFAULT_ROWS][DEFAULT_COLS];
		BufferedImage map = null;
		int caseHeight = MAP_H / DEFAULT_ROWS;
		int caseWidth = MAP_W / DEFAULT_COLS;
		int startingAtHeight = (MAP_H % DEFAULT_ROWS) / 2;
		int startingAtWidth = (MAP_W % DEFAULT_COLS) / 2;
		try{
			map = ImageIO.read(new File(Constante.MAP_PATH));
		}catch(IOException ex){
			System.out.println("Cannot load Map !");
		}
		
		for(int row = 0; row < DEFAULT_ROWS; row++){
			for(int col = 0; col < DEFAULT_COLS; col++){
				//checker dans la case correspondante qu'un max de pixels est bleu
				int bluePixels = 0;
				for(int i = startingAtHeight + row * caseHeight; i < startingAtHeight + (row + 1) * caseHeight; i++){
					for(int j = startingAtWidth + col * caseWidth; j < startingAtWidth + (col + 1) * caseWidth; j++){
						int rgb = map.getRGB(j,i);
						int red = (rgb >> 16) & 0xFF;
						int green = (rgb >> 8) & 0xFF;
						int blue = rgb & 0xFF;
						if(blue > green && blue > red){
							bluePixels++;
						}
					}
				}
				if((float)bluePixels / (caseHeight * caseWidth) > 0.75)
					data[row][col] = SEA;
				else
					data[row][col] = LAND;
			}
		}
		this.mapData = data;
		
		//affichage pour vérification
		for(int i = 0; i < DEFAULT_ROWS; i++){
			for(int j = 0; j < DEFAULT_COLS; j++){
				if(data[i][j] == LAND)
					System.out.print("# ");
				else
					System.out.print(". ");
			}
			System.out.println("");
		}
		
	}
}
