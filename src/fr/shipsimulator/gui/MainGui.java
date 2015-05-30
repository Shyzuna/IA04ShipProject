package fr.shipsimulator.gui;

import java.io.FileInputStream;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;

public class MainGui extends Application implements Runnable{

	private static EnvironnementAgent myAgent;
	private static Integer cols = Constante.DEFAULT_COLS;
	private static Integer rows = Constante.DEFAULT_ROWS;
	
	public static Integer getCols() {
		return cols;
	}

	public static void setCols(Integer cols) {
		MainGui.cols = cols;
	}

	public static Integer getRows() {
		return rows;
	}

	public static void setRows(Integer rows) {
		MainGui.rows = rows;
	}

	public static EnvironnementAgent getMyAgent() {
		return myAgent;
	}

	public static void setMyAgent(EnvironnementAgent myAgent) {
		MainGui.myAgent = myAgent;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		StackPane root = new StackPane();
		Scene scene = new Scene(root, 800, 600);
		
		//background
		Image map = new Image(new FileInputStream(Constante.MAP_PATH));
		ImageView background = new ImageView();
		background.setImage(map);
		background.setPreserveRatio(false);
		background.setCache(true);
		background.setFitHeight(Constante.MAP_H);
		background.setFitWidth(Constante.MAP_W);
		
		//Grille
		GridPane gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setGridLinesVisible(true); // grid visible
		for(int i = 0; i < MainGui.cols ; i++){
	    	gp.getColumnConstraints().add(new ColumnConstraints(Math.round((double)Constante.MAP_W/(double)MainGui.cols)));
			gp.getRowConstraints().add(new RowConstraints(Math.round((double)Constante.MAP_H/(double)MainGui.rows)));
		}
		
		Image boat_icone = new Image(new FileInputStream(Constante.BATEAU_PATH));
		
		/*test*/
		ImageView iv2;
		for (int row = 0; row < 30; row++) {
		    for (int col = 0; col < 30; col++) {
		    	iv2 = new ImageView();
		    	iv2.setImage(boat_icone);
		    	GridPane.setRowIndex(iv2, row);
		    	GridPane.setColumnIndex(iv2, col);
		    	gp.getChildren().add(iv2);
		    }
		}
		
		
		//Event resize scene
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	//remove constraints
		    	for(int i = gp.getColumnConstraints().size()-1; i >= 0 ; i--){
		    		gp.getColumnConstraints().remove(i);
		    	}
		    	//add new
		    	for(int i = 0; i < MainGui.cols ; i++){
			    	gp.getColumnConstraints().add(new ColumnConstraints(Math.round(newSceneWidth.doubleValue()/(double)MainGui.cols)));
				}
		    	//resize background
		    	background.setFitWidth(newSceneWidth.doubleValue());
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		    	//remove constraints
		    	for(int i = gp.getRowConstraints().size()-1; i >= 0 ; i--){
		    		gp.getRowConstraints().remove(i);
		    	}
		    	//add new
		    	for(int i = 0; i < MainGui.rows ; i++){
			    	gp.getRowConstraints().add(new RowConstraints(Math.round(newSceneHeight.doubleValue()/(double)MainGui.rows)));
				}
		    	//resize background
		    	background.setFitHeight(newSceneHeight.doubleValue());
		    }
		});
		
		//adding element
		root.getChildren().add(background);
		root.getChildren().add(gp);
		
		//construct scene
        primaryStage.setTitle("Ship Simulator");
        primaryStage.getIcons().add(boat_icone);
        primaryStage.setScene(scene);
        primaryStage.show();
	}

	@Override
	public void run() {
		launch();
	}
}
