package fr.shipsimulator.gui;

import jade.gui.GuiEvent;

import java.io.FileInputStream;

import javafx.scene.control.*;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.constantes.Constante;
import javafx.event.ActionEvent;

/**
 * 
 * @author Benoit
 * TODO : upgrade resize to perfect size : size/rows or cols  
 * snap or defined sizes
 */

public class MainGui extends Application implements Runnable{

	private static EnvironnementAgent myAgent;
	private static Integer cols = Constante.DEFAULT_COLS;
	private static Integer rows = Constante.DEFAULT_ROWS;
	private static Integer boat = 0;
	private static Integer city = 0;
	
	private StackPane root;
	private BorderPane bp;
	private ToolBar tb_bot;
	private static TextArea logArea;
	private ScrollPane logPane, statutPane;
	private ImageView background;
	private GridPane gp;
	private MenuBar menu;
	private Menu mv1;
	private MenuItem mi1;
	
	private Integer factor_grid_w;
	private Integer factor_grid_h;
	
	//Event used
	private boolean EatOneEventW = false;
	private boolean EatOneEventH = false;
	
	public static Integer getBoat() {
		return boat;
	}

	public static void setBoat(Integer boat) {
		MainGui.boat = boat;
	}

	public static Integer getCity() {
		return city;
	}

	public static void setCity(Integer city) {
		MainGui.city = city;
	}
	
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
		
		root = new StackPane();
		bp = new BorderPane();
		
		factor_grid_w = Constante.DEFAULT_FACTOR;
		factor_grid_h = Constante.DEFAULT_FACTOR;
		
		//toolbar bot & buttons
		//TODO METTRE IMAGE
		Button lecture = new Button("Lecture");
		
		lecture.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	MainGui.myAgent.postGuiEvent(new GuiEvent(this,Constante.START));
            }
        });
		
		Button pause = new Button("Pause");
		
		pause.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	MainGui.myAgent.postGuiEvent(new GuiEvent(this,Constante.SUSPEND));
            }
        });
		
		Button stop = new Button("Stop");
		
		stop.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	MainGui.myAgent.postGuiEvent(new GuiEvent(this,Constante.STOP));
            }
        });
		
		tb_bot = new ToolBar(
				lecture,
				pause,
				stop);
		tb_bot.setPrefHeight(40); 
		
		//menu
		menu = new MenuBar();
		mv1 = new Menu("Simulation");
		mi1 = new MenuItem("Options");
		mv1.getItems().add(mi1);
		menu.setPrefHeight(20);
		menu.getMenus().add(mv1);
		mi1.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent t) {
            	OptionSimulation stage = new OptionSimulation(primaryStage);
            	stage.setOnHiding(new EventHandler<WindowEvent>() {
        			public void handle(WindowEvent we) {
        				double center_size_w = primaryStage.getWidth() - Constante.BORDER_W - statutPane.getPrefWidth() - logPane.getPrefWidth();
        				double center_size_h = primaryStage.getHeight() - Constante.BORDER_H - tb_bot.getPrefHeight() - menu.getPrefHeight();
        				gp.getColumnConstraints().clear();
        				gp.getRowConstraints().clear();
        				
        				for(int i = 0; i < MainGui.cols ; i++){
        			    	gp.getColumnConstraints().add(new ColumnConstraints(Math.round(center_size_w/(double)MainGui.cols)));
        				}
        				for(int i = 0; i < MainGui.rows ; i++){
        					gp.getRowConstraints().add(new RowConstraints(Math.round(center_size_h/(double)MainGui.rows)));
        				}
        			}
        		});
            	stage.show(); 
            }
        });
		
		//log side
		logArea = new TextArea();
		logArea.prefWidth(150);
	         
        logPane = new ScrollPane();
        logPane.setContent(logArea);
        logPane.setFitToWidth(true);
        logPane.setPrefWidth(150);
        logPane.setFitToHeight(true);
		
        //statut side
        statutPane = new ScrollPane();
        statutPane.setFitToWidth(true);
        statutPane.setPrefWidth(150);
        statutPane.setFitToHeight(true);
        statutPane.setContent(new Label("Statut"));
        
        //center size
		double center_size_w = Constante.DEFAULT_FACTOR*MainGui.cols;
		double center_size_h = Constante.DEFAULT_FACTOR*MainGui.rows;
		
		//background
		Image map = new Image(new FileInputStream(Constante.MAP_PATH));
		background = new ImageView();
		background.setImage(map);
		background.setPreserveRatio(false);
		background.setCache(true);
		background.setFitHeight(center_size_h);
		background.setFitWidth(center_size_w);
		
		//Grille
		gp = new GridPane();
		gp.setAlignment(Pos.CENTER);
		gp.setGridLinesVisible(true); // grid visible
		for(int i = 0; i < MainGui.cols ; i++){
	    	gp.getColumnConstraints().add(new ColumnConstraints(Math.round(center_size_w/(double)MainGui.cols)));
			gp.getRowConstraints().add(new RowConstraints(Math.round(center_size_h/(double)MainGui.rows)));
		}
		
		Image boat_icone = new Image(new FileInputStream(Constante.BATEAU_PATH));
		
		/*test
		ImageView iv2;
		for (int row = 0; row < 30; row++) {
		    for (int col = 0; col < 30; col++) {
		    	iv2 = new ImageView();
		    	iv2.setImage(boat_icone);
		    	GridPane.setRowIndex(iv2, row);
		    	GridPane.setColumnIndex(iv2, col);
		    	gp.getChildren().add(iv2);
		    }
		}*/
		
		Scene scene = new Scene(bp, Constante.DEFAULT_FACTOR*MainGui.cols+logPane.getPrefWidth()+statutPane.getPrefWidth(), Constante.DEFAULT_FACTOR*MainGui.rows+tb_bot.getPrefHeight()+menu.getPrefHeight());
		//Event resize scene
		scene.widthProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
		    	if(!EatOneEventW){
		    		EatOneEventW = true;
			    	Double dif = newSceneWidth.doubleValue()-oldSceneWidth.doubleValue();
			    	Integer coef = 1;
			    	if(dif < 0){
			    		coef = -1;
			    		dif = Math.abs(dif);
			    	}
			    	
			    	Integer dif_factor = (int) Math.ceil(dif/(double)MainGui.cols);
			    	
			    	factor_grid_w += coef * dif_factor; 
			    	factor_grid_w = factor_grid_w > Constante.MAX_FACTOR ? Constante.MAX_FACTOR : factor_grid_w < Constante.MIN_FACTOR ? Constante.MIN_FACTOR : factor_grid_w;
			    	double new_width = factor_grid_w*MainGui.cols + statutPane.getPrefWidth() + logPane.getPrefWidth() + Constante.BORDER_W;
			    	primaryStage.setWidth(new_width);
			    	
			    	//remove constraints
			    	gp.getColumnConstraints().clear();
			    	//add new
			    	for(int i = 0; i < MainGui.cols ; i++){
				    	gp.getColumnConstraints().add(new ColumnConstraints(factor_grid_w));
					}
			    	//resize background
			    	background.setFitWidth(factor_grid_w*MainGui.cols);
		    	} else {
		    		EatOneEventW = false;
			    }
		    }
		});
		scene.heightProperty().addListener(new ChangeListener<Number>() {
		    @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
		    	if(!EatOneEventH){
		    		EatOneEventH = true;
		    		Double dif = newSceneHeight.doubleValue()-oldSceneHeight.doubleValue();
			    	Integer coef = 1;
			    	if(dif < 0){
			    		coef = -1;
			    		dif = Math.abs(dif);
			    	}
			    	
			    	Integer dif_factor = (int) Math.ceil(dif/(double)MainGui.rows);
			    	
			    	factor_grid_h += coef * dif_factor; 
			    	factor_grid_h = factor_grid_h > Constante.MAX_FACTOR ? Constante.MAX_FACTOR : factor_grid_h < Constante.MIN_FACTOR ? Constante.MIN_FACTOR : factor_grid_h;
			    	double new_height = factor_grid_h*MainGui.rows + tb_bot.getPrefHeight() + menu.getPrefHeight() + Constante.BORDER_H;
			    	primaryStage.setHeight(new_height);
			    	
			    	//remove constraints
					gp.getRowConstraints().clear();
			    	//add new
			    	for(int i = 0; i < MainGui.rows ; i++){
				    	gp.getRowConstraints().add(new RowConstraints(factor_grid_h));
					}
			    	//resize background
			    	background.setFitHeight(factor_grid_h*MainGui.rows);
		    	}else{
		    		EatOneEventH = false;
		    	}
		    }
		});
		//closing event
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			public void handle(WindowEvent we) {
				MainGui.myAgent.postGuiEvent(new GuiEvent(this,Constante.QUIT));
          	}
		});       

		//adding element
		root.getChildren().add(background);
		root.getChildren().add(gp);
		
		bp.setBottom(tb_bot);
		bp.setLeft(logPane);
		bp.setTop(menu);
		bp.setRight(statutPane);
		bp.setCenter(root);
		
		//construct scene
		primaryStage.setMinHeight(Constante.MIN_FACTOR*MainGui.rows+tb_bot.getPrefHeight()+menu.getPrefHeight());
		primaryStage.setMinWidth(Constante.MIN_FACTOR*MainGui.cols+logPane.getPrefWidth()+statutPane.getPrefWidth());
        primaryStage.setTitle("Ship Simulator");
        primaryStage.getIcons().add(boat_icone);
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
        
        //define refresh Timertask
        Timer refresh = new Timer();
        refresh.scheduleAtFixedRate(new TimerTask(){
        	@Override
        	public void run(){
        		Platform.runLater(new Runnable(){
					@Override
					public void run() {
						//DO SMTH
					}
        		});
        	}
        }, 0, 1000);
        
	}
	
	public static void writeLog (String author, String content){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				logArea.setText(logArea.getText() + "\n"+author+">"+content);
			}
		});
	}
	
	@Override
	public void run() {
		launch();
	}
}
