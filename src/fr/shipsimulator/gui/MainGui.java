package fr.shipsimulator.gui;

import jade.gui.GuiEvent;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import fr.shipsimulator.agent.BoatAgent;
import fr.shipsimulator.agent.CityAgent;
import fr.shipsimulator.agent.EnvironnementAgent;
import fr.shipsimulator.agent.boatCrew.BoatCrewAgent;
import fr.shipsimulator.constantes.Constante;
import fr.shipsimulator.gui.component.TileClicked;
import fr.shipsimulator.structure.Mission;
import fr.shipsimulator.structure.Ressource;

/**
 * 
 * @author Benoit
 * TODO : /
 */

public class MainGui extends Application implements Runnable{

	private static EnvironnementAgent myAgent;
	private static Integer cols = Constante.DEFAULT_COLS;
	private static Integer rows = Constante.DEFAULT_ROWS;
	private static Integer boat = Constante.DEFAULT_BOAT;
	private static Integer city = Constante.DEFAULT_CITY;
	private static Integer factor_grid_w = Constante.DEFAULT_FACTOR;
	private static Integer factor_grid_h = Constante.DEFAULT_FACTOR;
	
	private StackPane root;
	private BorderPane bp;
	private ToolBar tb_bot;
	private static TextArea logArea;
	private ScrollPane logPane, statutPane;
	private static ImageView background;
	private GridPane gp;
	private static GridPane statutGrid;
	private MenuBar menu;
	private Menu mv1;
	private MenuItem mi1;

	//Event used
	private boolean EatOneEventW = false;
	private boolean EatOneEventH = false;
	
	
	public static ImageView getBackground() {
		return background;
	}
	
	public static Integer getFactor_grid_w() {
		return factor_grid_w;
	}
	
	public static Integer getFactor_grid_h() {
		return factor_grid_h;
	}

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

	public static int getFactorGridW(){
		return factor_grid_w;
	}
	
	public static int getFactorGridH(){
		return factor_grid_h;
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
            	OptionSimulation stage = new OptionSimulation(primaryStage,myAgent.getStateSimulation());
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
	    logArea.setEditable(false);  
        logPane = new ScrollPane();
        logPane.setContent(logArea);
        logPane.setFitToWidth(true);
        logPane.setPrefWidth(300);
        logPane.setFitToHeight(true);
		
        //statut side
        statutGrid = new GridPane();
        statutPane = new ScrollPane();
        statutPane.setFitToWidth(true);
        statutPane.setPrefWidth(150);
        statutPane.setFitToHeight(true);
        statutPane.setContent(statutGrid);
        
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
	    	gp.getColumnConstraints().add(new ColumnConstraints(Constante.DEFAULT_FACTOR));
			gp.getRowConstraints().add(new RowConstraints(Constante.DEFAULT_FACTOR));
		}
		
		Image boat_icone = new Image(new FileInputStream(Constante.BATEAU_PATH));
		Image city_icone = new Image(new FileInputStream(Constante.VILLE_PATH));

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
			    	
			    	MainGui.factor_grid_w += coef * dif_factor; 
			    	double new_width = MainGui.factor_grid_w*MainGui.cols + statutPane.getPrefWidth() + logPane.getPrefWidth() + Constante.BORDER_W;
			    	
			    	new_width = MainGui.factor_grid_w*MainGui.cols + statutPane.getPrefWidth() + logPane.getPrefWidth() + Constante.BORDER_W;
			    	MainGui.factor_grid_h = new_width > Constante.MAX_W ? MainGui.factor_grid_w-1:new_width < Constante.MIN_W ? MainGui.factor_grid_w+1:MainGui.factor_grid_w;
			    	primaryStage.setWidth(new_width);
			    	
			    	//remove constraints
			    	gp.getColumnConstraints().clear();
			    	//add new
			    	for(int i = 0; i < MainGui.cols ; i++){
				    	gp.getColumnConstraints().add(new ColumnConstraints(factor_grid_w));
					}
			    	//resize background
			    	background.setFitWidth(MainGui.factor_grid_w*MainGui.cols);
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
			    	
			    	MainGui.factor_grid_h += coef * dif_factor; 
			    	double new_height = MainGui.factor_grid_h*MainGui.rows + tb_bot.getPrefHeight() + menu.getPrefHeight() + Constante.BORDER_H;
			    	
			    	MainGui.factor_grid_h = new_height > Constante.MAX_H ? MainGui.factor_grid_h-1:new_height < Constante.MIN_H ? MainGui.factor_grid_h+1:MainGui.factor_grid_h;
			    	
			    	new_height = MainGui.factor_grid_h*MainGui.rows + tb_bot.getPrefHeight() + menu.getPrefHeight() + Constante.BORDER_H;
			    	
			    	primaryStage.setHeight(new_height);
			    	
			    	//remove constraints
					gp.getRowConstraints().clear();
			    	//add new
			    	for(int i = 0; i < MainGui.rows ; i++){
				    	gp.getRowConstraints().add(new RowConstraints(factor_grid_h));
					}
			    	//resize background
			    	background.setFitHeight(MainGui.factor_grid_h*MainGui.rows);
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
		primaryStage.setMinHeight(Constante.MIN_H);
		primaryStage.setMinWidth(Constante.MIN_W);
        primaryStage.setTitle("Ship Simulator");
        primaryStage.getIcons().add(boat_icone);
        primaryStage.setScene(scene);
        primaryStage.show();
        
        //define refresh Timertask
        Timer refresh = new Timer();
        refresh.scheduleAtFixedRate(new TimerTask(){
        	@Override
        	public void run(){
        		Platform.runLater(new Runnable(){
					@Override
					public void run() {
						if(myAgent.getStateSimulation() == Constante.RUNNING){
							ImageView iv;
							gp.getChildren().clear();
							for(CityAgent ca : myAgent.getListCity()){
								iv = new ImageView();
								iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new TileClicked(ca));
						    	iv.setImage(city_icone);
						    	GridPane.setRowIndex(iv, ca.getCity().getPosY());
						    	GridPane.setColumnIndex(iv, ca.getCity().getPosX());
						    	gp.getChildren().add(iv);
							}
							for(BoatAgent ba : myAgent.getListBoat()){
								iv = new ImageView();
								iv.addEventHandler(MouseEvent.MOUSE_CLICKED, new TileClicked(ba));
						    	iv.setImage(boat_icone);
						    	GridPane.setRowIndex(iv, ba.getBoat().getPosY());
						    	GridPane.setColumnIndex(iv, ba.getBoat().getPosX());
						    	gp.getChildren().add(iv);
							}
						}
					}
        		});
        	}
        }, 0, 1000);
        
	}
	
	public static void writeLog (String author, String content){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				logArea.setText(logArea.getText()+author+">"+content + "\n");
			}
		});
	}
	
	public static void clearLog(){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {
				logArea.setText("");
			}
		});
	}
	
	public static void writeBoatStatus (BoatAgent agent){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {				
				statutGrid.getChildren().clear();
				int rowIndex = 0;
				//HBox hbox_img_name = new HBox();
				Image icone;
				ImageView iv = new ImageView();
				try {
					icone = new Image(new FileInputStream(Constante.BATEAU_PATH));
					iv.setImage(icone);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}					
				statutGrid.addRow(rowIndex++, iv, new Label(agent.getLocalName()));
				statutGrid.addRow(rowIndex++, new Label(""));
				statutGrid.addRow(rowIndex++, new Label("Position : "), new Label(agent.getBoat().getPosX() + ", " + agent.getBoat().getPosY()));
				statutGrid.addRow(rowIndex++, new Label(""));
				statutGrid.addRow(rowIndex++, new Label("Cargaison :"));
				//ajouter map de la cargaison
				Map<Integer, Integer> boatResources = agent.getBoat().getResources();
				if(boatResources != null){
					Ressource res = Ressource.WOOD;
					for(Map.Entry<Integer,Integer> entry : boatResources.entrySet()) {
						for (Ressource r : Ressource.values()) {
							if(r.ordinal() == entry.getKey()){
								res = r;
								break;
							}
						}
						statutGrid.addRow(rowIndex++, new Label(""+ res.name()), new Label("" + entry.getValue()));
					}
				}
				statutGrid.addRow(rowIndex++, new Label(""));
				statutGrid.addRow(rowIndex++, new Label("Equipage :"));
				//ajouter liste de l'équipage
				List<BoatCrewAgent> boatCrew = agent.getBoat().getCrewMembers();
				if(boatCrew != null) {
					for(int i = 0; i < boatCrew.size(); i++){
						statutGrid.addRow(rowIndex++, new Label(boatCrew.get(i).getLocalName()));
					}
				}
			}
		});
	}
	
	public static void writeCityStatus (CityAgent agent){
		Platform.runLater(new Runnable(){
			@Override
			public void run() {		
				int rowIndex = 0;
				statutGrid.getChildren().clear();
				//HBox hbox_img_name = new HBox();
				Image icone;
				ImageView iv = new ImageView();
				try {
					icone = new Image(new FileInputStream(Constante.VILLE_PATH));
					iv.setImage(icone);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				statutGrid.addRow(rowIndex++, iv, new Label(agent.getLocalName()));
				statutGrid.addRow(rowIndex++, new Label(""));
				statutGrid.addRow(rowIndex++, new Label("Position : "), new Label(agent.getCity().getPosX() + ", " + agent.getCity().getPosY()));
				statutGrid.addRow(rowIndex++, new Label(""));
				statutGrid.addRow(rowIndex++, new Label("Stocks :"));
				//ajouter map des stocks de la ville
				Map<Integer, Integer> cityResources = agent.getCity().getResources();
				if(cityResources != null){
					Ressource res = Ressource.WOOD;
					for(Map.Entry<Integer,Integer> entry : cityResources.entrySet()) {
						for (Ressource r : Ressource.values()) {
							if(r.ordinal() == entry.getKey()){
								res = r;
								break;
							}
						}
						statutGrid.addRow(rowIndex++, new Label(""+ res.name()), new Label("" + entry.getValue()));
					}
				}
				
				statutGrid.addRow(rowIndex++, new Label("Missions :"));
				//missions dispo dans la ville
				List<Mission> missions = myAgent.getMissionAgent().getMissions();
				for(Mission m: missions){
					if(m.getDeparture().equals(agent.getCity())){
						statutGrid.addRow(rowIndex++, new Label("Mission" + m.getId()));
					}
				}
			}
		});
	}
	
	@Override
	public void run() {
		launch();
	}
}
