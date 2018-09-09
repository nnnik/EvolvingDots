package nnnik.ai.evolution.vector_steps_1;

import java.util.*;
import javafx.scene.canvas.GraphicsContext;

public class Population {
	
	private ArrayList<Dot> units = null;
	private int generation = 0;
	
	private Dot best;
	
	Random r = new Random();
	int brainSize;
	
	public Population(int size, int brainSize) {
		units = new ArrayList<>();
		this.brainSize = brainSize;
		setSize(size);
	}
	
	public void expandBrainSize(int n) {
		for(int i=0;i<units.size();i++) {
			units.get(i).getBrain().fillRandom(n);
		}
	}
	
	public void setSize(int n) {
		for(;units.size()<n;) {
			units.add(new Dot(brainSize));
		}
	}
	
	public void draw(GraphicsContext gc) {
		for (int i = 0; i<units.size(); i++) {
			units.get(i).draw(gc);
		}
	}
	
	public void move(double timeMultiplier) {
		for (int i = 0; i<units.size(); i++) {
			units.get(i).move(timeMultiplier);
		}
	}
	
	public void think(int step) {
		for (int i = 0; i<units.size(); i++) {
			units.get(i).think(step);
		}
	}
	
	public boolean allInactive() {
		for (int i = 0; i<units.size(); i++) {
			if (units.get(i).isActive()) {
				return false;
			}
		}
		return true;
	}
	
	public void evolve() {
		double totalFitness = getTotalFitness();
		ArrayList<Dot> nextGen = new ArrayList<>(units.size());
		nextGen.add(getBest());
		for (int i=1; i<units.size(); i++) {
			double selectedFitness = r.nextDouble() * totalFitness;
			for (int j=0; i<units.size(); j++) {
				selectedFitness -= units.get(j).getFitness();
				if (selectedFitness<0) {
					nextGen.add(units.get(j).generateBaby());
					break;
				}
			}
		}
		units = nextGen;
		generation++;
	}
	
	public double getTotalFitness() {
		double total = 0;
		double max = -100000.0;
		double fit;
		for (int i=0; i<units.size(); i++) {
			fit = units.get(i).getFitness();
			total += fit;
			if (fit>max) {
				best = units.get(i);
				max = fit;
			}
		}
		best = best.clone();
		return total;
	}
	
	public Dot getBest() {
		return best;
	}

	/**
	 * @return the generation
	 */
	public int getGeneration() {
		return generation;
	}
	
	public boolean hasWon() {
		return best.hasWon();
	}
	
}
