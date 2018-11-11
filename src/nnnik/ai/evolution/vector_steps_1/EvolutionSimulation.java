package nnnik.ai.evolution.vector_steps_1;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import nnnik.ai.evolution.gene.GeneticPopulation;
import nnnik.ai.evolution.gene.SimulationResults;
import nnnik.ai.evolution.maze_game.Goal;
import nnnik.ai.evolution.maze_game.Maze;
import nnnik.ai.evolution.maze_game.MazeBuilder;
import nnnik.ai.evolution.maze_game.MazeContender;
import nnnik.maths.geometry.Vector2;

public class EvolutionSimulation {
	private Canvas display = null;
	private Timeline mainLoop = null;
	
	private GeneticPopulation population = null;
	
	private Maze maze;
	
	int currentThonkFrame = 0;
	
	int thonkEveryNFrame = 10;
	int generation = 0;
	int increaseEveryNGeneration = 5;
	int increaseSize = 2;
	double movementSpeed = 150.0;
	int brainSize = 5;
	int populationSize = 2000;
	
	private Vector2 canvasSize = new Vector2(1000.0, 1000.0);
	private boolean gameUnfinished = true;
	
	public EvolutionSimulation() {
		display = new Canvas(canvasSize.getX(), canvasSize.getY());
		buildMaze(new MazeBuilder());
		populate();
		prepareSimulation();
	}
	
	public void startSimulation() {
		mainLoop.play();
	}
	
	public void pauseSimulation() {
		mainLoop.pause();
	}
	
	public void stopSimulation() {
		mainLoop.stop();
	}
	
	public Node getDisplay() {
		return display;
	}
	
	private void buildMaze(MazeBuilder mb) {
		mb.setGoal(new Vector2(0.0, -9.0));
		mb.setSpawn(new Vector2(0.0, 9.0));
		mb.addBlockade(new Vector2(-7.5, -2.7), new Vector2(2.5, 0.25));
		mb.addBlockade(new Vector2(3.5, -2.7), new Vector2(6.5, 0.25));
		mb.addBlockade(new Vector2(-5, -5.8), new Vector2(5, 0.25));
		for (int i=0; i<populationSize; i++) {
			mb.addContender(new MovementBrain());
		}
		maze = mb.getMaze();
	}
	
	private void populate() {
		population = new GeneticPopulation(new ImplGeneFactory(), populationSize, brainSize);
	}
	
	private void buildTimeLine() {
		final double timeMultiplier = 1.0/((double)thonkEveryNFrame)*movementSpeed;
		mainLoop = new Timeline(new KeyFrame(Duration.seconds(1/60.0), new EventHandler<ActionEvent>() {
        	private int currentFrame = 0;
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
				if (currentFrame % thonkEveryNFrame == 0) {
					currentFrame = 0;
					thonk();
				}
				currentFrame++;
				updatePhysics(timeMultiplier);
				redraw();
				active = false;
			}
		}));
		mainLoop.setCycleCount(Timeline.INDEFINITE);
	}
	
	private void prepareSimulation() {
		generation++;
		if (Math.floorMod(generation, increaseEveryNGeneration) == 0 && gameUnfinished) {
			population.expandBrainSize(increaseSize);
		}
		for (int i = 0; i<populationSize; i++) {
			MazeContender contender = maze.getContenders().get(i);
			MovementBrain brain = (MovementBrain) contender.getInputProvider();
			brain.reset();
			brain.setGenome(population.getGenomes().get(i));
		}
		buildTimeLine();
	}
	
	private boolean updatePopulation() {
		if (maze.allInactive()) {
			mainLoop.stop();
			SimulationResults results = gatherResults();
			population.evolve(results);
			maze.reset();
			prepareSimulation();
			startSimulation();
			maze.getContenders().get(populationSize-1).setHighlightColor(Color.CORNFLOWERBLUE);
			return true;
		}
		return false;
	}
	
	private SimulationResults gatherResults() {
		return new FitnessEvaluator(maze.getContenders(), (Goal) maze.getChildren().get(0));
	}

	private void updatePhysics(double timeModifier) {
		maze.update(timeModifier);
	}
	
	private void redraw() {
		GraphicsContext gc = display.getGraphicsContext2D();
		gc.save();
		Vector2 multipliers = canvasSize.clone();
		multipliers.divide(maze.getSize());
		multipliers.multiply(0.5);
		gc.scale(multipliers.getX(), multipliers.getY());
		gc.translate(maze.getSize().getX(), maze.getSize().getY());
		maze.draw(gc);
		gc.restore();
	}
	
	private void thonk() {
		for (MazeContender contender: maze.getContenders()) {
			MovementBrain brain = (MovementBrain) contender.getInputProvider();
			brain.think(contender);
		}
	}
}
