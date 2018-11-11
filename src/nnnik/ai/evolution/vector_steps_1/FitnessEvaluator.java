package nnnik.ai.evolution.vector_steps_1;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import nnnik.ai.evolution.gene.Gene;
import nnnik.ai.evolution.gene.SimulationResults;
import nnnik.ai.evolution.maze_game.Goal;
import nnnik.ai.evolution.maze_game.MazeContender;

public class FitnessEvaluator implements SimulationResults {
	
	Map<List<Gene>, DoubleWorkaround> fitnesses = new HashMap<List<Gene>, DoubleWorkaround>();
	
	
	private class DoubleWorkaround { //distusting piece of shit class
		//for javas ugly restriction and bad language design
		double value;
		DoubleWorkaround(double value) {
			this.value = value;
		}
	}
	
	public FitnessEvaluator(List<MazeContender> contenders, Goal goal) {
		for (MazeContender contender: contenders) {
			double fitness = calculateFitness(contender, goal);
			MovementBrain brain = (MovementBrain) contender.getInputProvider();
			fitnesses.put(brain.getGenome(), new DoubleWorkaround(fitness));
		}
		
	}
	
	private double calculateFitness(MazeContender contender, Goal goal) {
		double fitness = 0;
		if (contender.isUnfinished()) {
			fitness = 1/(1+contender.getPosition().distance(goal.getPosition()));
			if (!contender.isAlive()) {
				fitness = Math.pow(fitness, 2);
			}
		} else {
			fitness = 1 + (1/contender.getTimeAlive());
		}
		return Math.pow(fitness, 10);
	}


	@Override
	public double getFitness(List<Gene> individual) {
		return fitnesses.get(individual).value;
	}}
