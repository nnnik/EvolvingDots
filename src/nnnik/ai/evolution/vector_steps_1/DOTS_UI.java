package nnnik.ai.evolution.vector_steps_1;


import java.util.logging.ConsoleHandler;
import java.util.logging.LogRecord;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.*;
import javafx.scene.paint.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.canvas.*;

public class DOTS_UI extends Application {
	
	private Canvas display = null;
	private Timeline mainLoop = null;
	
	private Population population = null;
	
	private int step;
	
	int generation = 0;
	int increaseEveryNGeneration = 5;
	int increaseSize = 5;
	int thonkEveryNFrame = 10;
	double movementSpeed = 100.0;
	int brainSize = 5;
	int populationSize = 500;
	
	@Override
	public void start(Stage stage) throws Exception {
		buildUI(stage);
		populate();
		startGeneration();
	}
	
	private void startGeneration() {
		step = 0;
		generation++;
		if (Math.floorMod(generation, increaseEveryNGeneration) == 0 && !population.hasWon()) {
			population.expandBrainSize(increaseSize);
		}
		buildTimeLine();
		mainLoop.play();
	}
	
	private void buildUI(Stage stage) {
		display = new Canvas(1000,1000);
		Group root = new Group();
		root.getChildren().add(display);
		Scene s = new Scene(root);
        stage.setTitle("Dots View");
        stage.setScene(s); 
        stage.sizeToScene(); 
        stage.show();
	}
	
	private void buildTimeLine() {
		final double timeMultiplier = 1.0/((double)thonkEveryNFrame)*movementSpeed;
		mainLoop = new Timeline(new KeyFrame(Duration.seconds(1/60.0), new EventHandler<ActionEvent>() {
        	
			private int frameCount = 0;
			private boolean active = false;
			@Override
			public void handle(ActionEvent event) {
				if (active) {
					return;
				}
				active = true;
				if (updatePopulation()) {
					return;
				}
				if (Math.floorMod(frameCount, thonkEveryNFrame) == 0) {
					thonk();
				}
				updatePhysics(timeMultiplier);
				redraw();
				frameCount++;
				active = false;
			}
		}));
		mainLoop.setCycleCount(Timeline.INDEFINITE);
	}
	
	private void populate() {
		population = new Population(populationSize, brainSize);
	}
	
	private boolean updatePopulation() {
		if (population.allInactive()) {
			mainLoop.stop();
			population.evolve();
			startGeneration();
			return true;
		}
		return false;
	}
	
	private void updatePhysics(double timeModifier) {
		population.move(timeModifier);
	}
	
	private void thonk() {
		population.think(step);
		step++;
	}
	
	private void redraw() {
		GraphicsContext gc = display.getGraphicsContext2D();
		gc.clearRect(0, 0, 1000, 1000);
		population.draw(gc);
		if (population.getBest() != null) {
			gc.setFill(Color.GREEN);
			Vector2 pos = population.getBest().getPosition();
			gc.fillOval(pos.getX(), pos.getY(), 10, 10);
		}
		gc.setFill(Color.BLACK);
		gc.fillRect(0, 200, 500, 25);
		gc.fillRect(0, 350, 200, 25);
		gc.fillRect(300, 350, 700, 25);
		gc.setFill(Color.GOLD);
		gc.fillOval(480, 30, 40, 40);
	}
	
	public static void main(String[] args) {
		Application.launch(args);
	}

	public static void log(String msg) {
		ConsoleHandler o = new ConsoleHandler();
		o.publish(new LogRecord(o.getLevel(), msg));
		o.flush();
	}
    
}
