package nnnik.ai.evolution.vector_steps_1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import nnnik.ai.evolution.gene.GenomeContainer;
import nnnik.ai.evolution.gene.SimulationResults;
import nnnik.ai.evolution.maze_game.Goal;
import nnnik.ai.evolution.maze_game.MazeContender;

public class FitnessEvaluator implements SimulationResults {
	
	Map<GenomeContainer, DoubleWorkaround> fitnesses = new HashMap<GenomeContainer, DoubleWorkaround>();
	
	
	private class DoubleWorkaround { //distusting piece of shit class
		//for javas ugly restriction and bad language design
		double value;
		DoubleWorkaround(double value) {
			this.value = value;
		}
	}
	
	public FitnessEvaluator(ArrayList<GenomeContainer> arrayList, Goal goal) {
		for (GenomeContainer brainG: arrayList) {
			MovementBrain brain = (MovementBrain) brainG;
			double fitness = calculateFitness(brain.getContender(), goal);
			fitnesses.put(brainG, new DoubleWorkaround(fitness));
		}
		
	}
	
	private double calculateFitness(MazeContender contender, Goal goal) {
		double fitness = 0;
		if (contender.isUnfinished()) {
			fitness = 1/(1+contender.getPosition().distance(goal.getPosition()));
		} else {
			fitness = 1 + (1/contender.getTimeAlive());
		}
		return Math.pow(fitness, 10);
	}


	@Override
	public double getFitness(GenomeContainer individual) {
		return fitnesses.get(individual).value;
	}}
