package nnnik.ai.evolution.vector_steps_1;

import java.util.*;

import nnnik.ai.evolution.gene.DirectionGene;
import nnnik.ai.evolution.gene.DistanceGene;
import nnnik.ai.evolution.gene.Gene;
import nnnik.ai.evolution.gene.SequenceGene;
import nnnik.ai.evolution.maze_game.InputProvider;
import nnnik.ai.evolution.maze_game.MazeContender;
import nnnik.maths.geometry.Vector2;

public class MovementBrain implements InputProvider {
	private ArrayList<Gene> genome = null;
	private Gene controlSequence = new SequenceGene();
	private int position = -1;
	private Vector2 distanceCenterPosition = null;
	private Vector2 currentOutput = null;
	
	public void setGenome(ArrayList<Gene> genome) {
		this.genome = genome;
	}
	
	public ArrayList<Gene> getGenome() {
		return genome;
	}
	
	public void reset() {
		controlSequence = new SequenceGene();
		position = -1;
		distanceCenterPosition = null;
	}
	
	public Vector2 generateInput(MazeContender individual) {
		return currentOutput;
	}
	
	public void think(MazeContender individual) {
		if (genome == null) {
			currentOutput = null;
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
			currentOutput = (DirectionGene) genome.get(position);
		} else {
			currentOutput = null;
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
	
	@SuppressWarnings("unchecked")
	@Override
	public MovementBrain clone() {
		MovementBrain clone = new MovementBrain();
		clone.setGenome((ArrayList<Gene>) genome.clone());
		return clone;
		
	}
	
	public String toString() {
		String str = "";
		for (int i = 0; i<genome.size(); i++) {
			str = str + " " + genome.get(i).toString();
		}
		return str;
	}
}