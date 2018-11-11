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
import nnnik.ai.evolution.gene.GenomeContainer;
import nnnik.ai.evolution.gene.PopulationControl;
import nnnik.ai.evolution.gene.SimulationResults;
import nnnik.ai.evolution.maze_game.Goal;
import nnnik.ai.evolution.maze_game.Maze;
import nnnik.ai.evolution.maze_game.MazeBuilder;
import nnnik.ai.evolution.maze_game.MazeContender;
import nnnik.maths.geometry.Vector2;

public class EvolutionSimulation {
	
	private static final boolean HUMAN_MODE = true;
	
	private Canvas display = null;
	private KeyboardBrain humanInput = null;
	private Timeline mainLoop = null;
	
	private GeneticPopulation population = null;
	
	private Maze maze;
	private MazeBuilder builder;
	private Goal goal;
		
	private int thonkEveryNFrame = 10;
	private int generation = 0;
	private int increaseEveryNGeneration = 5;
	private int increaseSize = 2;
	private double movementSpeed = 150.0;
	private int brainSize = 5;
	private int populationSize = 1000;
	
	private Vector2 canvasSize = new Vector2(1000.0, 1000.0);
	private boolean gameUnfinished = true;
	
	private MazeContender best;
	
	public EvolutionSimulation() {
		display = new Canvas(canvasSize.getX(), canvasSize.getY());
		builder = new MazeBuilder();
		buildMaze(builder);
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
	
	public KeyboardBrain getInput() {
		return humanInput;
	}
	
	private void buildMaze(MazeBuilder mb) {
		goal = mb.setGoal(new Vector2(0.0, -9.0));
		mb.setSpawn(new Vector2(0.0, 9.0));
		mb.addBlockade(new Vector2(-7.5, -2.7), new Vector2(2.5, 0.25));
		mb.addBlockade(new Vector2(3.5, -2.7), new Vector2(6.5, 0.25));
		mb.addBlockade(new Vector2(-5, -5.8), new Vector2(5, 0.25));
		maze = mb.getMaze();
	}
	
	private class ContenderControl implements PopulationControl {

		@Override
		public GenomeContainer createIndividual() {
			MazeContender contender = builder.addContender();
			MovementBrain individual = new MovementBrain(contender);
			return individual;
		}

		@Override
		public void destroyIndividual(GenomeContainer individual) {
			MazeContender contender = ((MovementBrain) individual).getContender();
			maze.getContenders().remove(contender);
		}
		
	}
	
	private void populate() {
		if (HUMAN_MODE) {
			population = new GeneticPopulation(new ImplGeneFactory(), new ContenderControl(), 0, 0);
			humanInput = new KeyboardBrain(builder.addContender());
			
		} else {
			population = new GeneticPopulation(new ImplGeneFactory(), new ContenderControl(), populationSize, brainSize);
		}
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
		buildTimeLine();
	}
	
	private boolean updatePopulation() {
		if (maze.allInactive()) {
			mainLoop.stop();
			if (!HUMAN_MODE) {
				SimulationResults results = gatherResults();
				population.evolve(results);
				if (best != null) {
					best.setHighlightColor(null);
				}
				best = ((MovementBrain)population.getBest()).getContender();
				best.setHighlightColor(Color.CORNFLOWERBLUE);
				gameUnfinished = best.isUnfinished();
			}
			maze.reset();
			if (HUMAN_MODE) {
				humanInput.think();
			}
			prepareSimulation();
			startSimulation();
			return true;
		}
		return false;
	}
	
	private SimulationResults gatherResults() {
		return new FitnessEvaluator(population.getPopulation(), (Goal) goal);
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
		for (GenomeContainer brainG: population.getPopulation()) {
			((MovementBrain) brainG).think();
		}
	}
}
