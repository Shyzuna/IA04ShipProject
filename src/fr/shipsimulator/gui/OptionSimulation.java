package fr.shipsimulator.gui;

import javafx.scene.control.Button;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.component.Selector;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class OptionSimulation extends Stage{
	public OptionSimulation(Stage parent, Integer simulationState){
		
		OptionSimulation me = this;
		
    	GridPane gp = new GridPane();
    	
    	gp.setHgap(5);
    	gp.setVgap(20);
    	
    	Label label_boat = new Label("Nombre Bateau : ");
    	Label label_city = new Label("Nombre Ville : ");
    	Label label_grid_col = new Label("Taille Grille Col : ");
    	Label label_grid_row = new Label("Taille Grille Row : ");
    	
    	Selector select_boat = new Selector(MainGui.getBoat());
    	Selector select_city = new Selector(MainGui.getCity());
    	Selector select_col = new Selector(MainGui.getCols());
    	Selector select_row = new Selector(MainGui.getRows());
    	
    	Button valid = new Button("Valider");
    	if(simulationState == Constante.RUNNING)
    		valid.setDisable(true);
    	valid.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                MainGui.setBoat(select_boat.value());
                MainGui.setCity(select_city.value());
                MainGui.setCols(select_col.value());
                MainGui.setRows(select_row.value());
                me.close();
            }
        });
    	
    	gp.add(label_boat,0,0);
    	gp.add(label_city,0,1);
    	gp.add(label_grid_col,0,2);
    	gp.add(label_grid_row,0,3);
    	gp.add(select_boat, 1, 0);
    	gp.add(select_city,1,1);
    	gp.add(select_col,1,2);
    	gp.add(select_row,1,3);
    	gp.add(valid, 0, 4);
    	
    	Image boat_icone = null;
		try {
			boat_icone = new Image(new FileInputStream(Constante.BATEAU_PATH));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
    	
		this.initOwner(parent);
		this.initModality(Modality.WINDOW_MODAL);
    	this.getIcons().add(boat_icone);
        this.setTitle("Option Simulation");
        this.setScene(new Scene(gp, 300, 220));
	}
}
