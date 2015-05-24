package fr.shipsimulator.agent;

import jade.gui.GuiAgent;
import jade.gui.GuiEvent;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import fr.shipsimulator.behaviour.EnvironnementBehaviour;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.SimulationGui;

public class EnvironnementAgent extends GuiAgent implements Constante{
	
	protected SimulationGui myGui;
	protected int[][] mapData;
	
	@Override
	protected void setup() {
		
		this.myGui = new SimulationGui(this);
		this.myGui.setVisible(true);
		
		fillMapData(Constante.MAP_PATH);
		
		this.addBehaviour(new EnvironnementBehaviour());
	}

	@Override
	protected void onGuiEvent(GuiEvent ev) {
		switch(ev.getType()){
			case QUIT:
		}
	}
	
	protected void fillMapData(String path){
		int[][] data = new int[GRID_H][GRID_W];
		BufferedImage map = null;
		int caseHeight = MAP_H / GRID_H;
		int caseWidth = MAP_W / GRID_W;
		int startingAtHeight = (MAP_H % GRID_H) / 2;
		int startingAtWidth = (MAP_W % GRID_W) / 2;
		try{
			map = ImageIO.read(new File(Constante.MAP_PATH));
		}catch(IOException ex){
			System.out.println("Cannot load Map !");
		}
		
		for(int row = 0; row < GRID_H; row++){
			for(int col = 0; col < GRID_W; col++){
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
		for(int i = 0; i < GRID_H; i++){
			for(int j = 0; j < GRID_W; j++){
				if(data[i][j] == LAND)
					System.out.print("# ");
				else
					System.out.print(". ");
			}
			System.out.println("");
		}
		
	}
}
