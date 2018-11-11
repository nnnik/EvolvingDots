package nnnik.ai.evolution.vector_steps_1;

import java.util.*;

import nnnik.ai.evolution.gene.DirectionGene;
import nnnik.ai.evolution.gene.DistanceGene;
import nnnik.ai.evolution.gene.Gene;
import nnnik.ai.evolution.gene.GenomeContainer;
import nnnik.ai.evolution.gene.SequenceGene;
import nnnik.ai.evolution.maze_game.MazeContender;
import nnnik.maths.geometry.Vector2;

public class MovementBrain implements GenomeContainer {
	private ArrayList<Gene> genome = null;
	private Gene controlSequence = new SequenceGene();
	private int position = -1;
	private Vector2 distanceCenterPosition = null;
	private MazeContender individual;
	
	
	public MovementBrain(MazeContender individual) {
		this.individual = individual;
	}
	
	public void setGenome(ArrayList<Gene> genome) {
		this.genome = genome;
		reset();
	}
	
	public ArrayList<Gene> getGenome() {
		return genome;
	}
	
	public MazeContender getContender() {
		return individual;
	}
	
	public void reset() {
		controlSequence = new SequenceGene();
		position = -1;
		distanceCenterPosition = null;
	}
	
	public void think() {
		if (genome == null) {
			individual.setAcceleration(null);
			return;
		}
		if (controlSequence instanceof DistanceGene) {
			if (distanceCenterPosition.distance(individual.getPosition()) > ((DistanceGene)controlSequence).getDistance()) {
				skipToNextControlSequence(individual);
			}
		} else if (controlSequence instanceof SequenceGene) {
			position++;
			if (position < genome.size() && !(genome.get(position) instanceof DirectionGene)) {
				enableControlSequence(individual);
			}
		}
		if (position < genome.size()) {
			individual.setAcceleration((DirectionGene) genome.get(position));
		} else {
			individual.setAcceleration(null);
		}
	}
	
	private void skipToNextControlSequence(MazeContender individual) {
		while (position < genome.size() && genome.get(position) instanceof DirectionGene) {
			position++;
		}
		if (position < genome.size()) {
			enableControlSequence(individual);
		}
	}
	
	private void enableControlSequence(MazeContender individual) {
		while (true) {
			controlSequence = genome.get(position);
			if (controlSequence instanceof DistanceGene) {
				distanceCenterPosition = individual.getPosition().clone();
			}
			position++;
			if (position >= genome.size() || genome.get(position) instanceof DirectionGene) {
				break;
			}
		}
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i<genome.size(); i++) {
			str = str + " " + genome.get(i).toString();
		}
		return str;
	}
}