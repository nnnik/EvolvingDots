package nnnik.ai.evolution.vector_steps_1;


import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

import javafx.application.Application;
import javafx.scene.Scene;
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
        stage.setTitle("Dots View");
        stage.setScene(s); 
        stage.sizeToScene(); 
        stage.show();
		evo.startSimulation();
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
