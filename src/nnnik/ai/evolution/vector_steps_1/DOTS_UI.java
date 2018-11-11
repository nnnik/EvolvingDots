package nnnik.ai.evolution.vector_steps_1;


import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.*;
import javafx.stage.Stage;

public class DOTS_UI extends Application {
	
	private EvolutionSimulation evo = new EvolutionSimulation();
	
	private static boolean debug = false;
	
	@Override
	public void start(Stage stage) throws Exception {
		Node display = evo.getDisplay();
		Group root = new Group();
		root.getChildren().add(display);
		Scene s = new Scene(root);
		if (evo.getInput() != null) {
			attatchInputs(evo.getInput(), s);
		}
		
        stage.setTitle("Dots View");
        stage.setScene(s); 
        stage.sizeToScene(); 
        stage.show();
		evo.startSimulation();
	}
	
	private void attatchInputs(KeyboardBrain input, Scene s) {
		s.addEventFilter(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case UP:
						input.setUpState(true);break;
					case DOWN:
						input.setDownState(true);break;
					case LEFT:
						input.setLeftState(true);break;
					case RIGHT:
						input.setRightState(true);break;
					default:
						break;
				}
			}
		});
		s.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
			public void handle(KeyEvent event) {
				switch (event.getCode()) {
					case UP:
						input.setUpState(false);break;
					case DOWN:
						input.setDownState(false);break;
					case LEFT:
						input.setLeftState(false);break;
					case RIGHT:
						input.setRightState(false);break;
					default:
						break;
				}
			}
		});
	}

	public static void main(String[] args) {
		Application.launch(args);
	}

	public static void log(String msg) {
		if (debug) {
			ConsoleHandler o = new ConsoleHandler();
			o.publish(new LogRecord(o.getLevel(), msg));
			o.flush();
		}
	}
    
}
