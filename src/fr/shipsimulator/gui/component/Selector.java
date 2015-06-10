package fr.shipsimulator.gui.component;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class Selector extends HBox {
	
	private TextField text;
	private Button more,minus;
	private Selector linked;
	
	public Selector(Integer defaultVal, Selector linked){
		text = new TextField(defaultVal == null ? "0":defaultVal.toString());
		more = new Button("+");
		this.linked = linked;
		
		more.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer current = Integer.parseInt(text.getText());
                current++;
                if(linked.value() < current){
                	linked.setValue(current);
                }
                text.setText(current.toString());
            }
        });
		
		minus = new Button("-");
		
		minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer current = Integer.parseInt(text.getText());
                current = current == 0 ? 0:current-1;
                text.setText(current.toString());
            }
        });
		
		text.setEditable(false);
		this.getChildren().addAll(text,more,minus);
	}
	
	public Selector(Integer defaultVal){
		text = new TextField(defaultVal == null ? "0":defaultVal.toString());
		more = new Button("+");
		
		more.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer current = Integer.parseInt(text.getText());
                current++;
                text.setText(current.toString());
            }
        });
		
		minus = new Button("-");
		
		minus.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Integer current = Integer.parseInt(text.getText());
                current = current == 0 ? 0:current-1;
                text.setText(current.toString());
            }
        });
		
		text.setEditable(false);
		this.getChildren().addAll(text,more,minus);
	}
	
	public Integer value(){
		return Integer.parseInt(text.getText());
	}
	
	public void setValue(Integer val){
		text.setText(val.toString());
	}
}
