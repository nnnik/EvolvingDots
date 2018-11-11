package nnnik.ai.evolution.gene;

import java.util.Random;
import java.util.ArrayList;

public class GeneticPopulation {
	
	private static final double STRUCTURAL_STABLE_MUTATION_CHANCE = 0.0;
	private static final double WEAK_STABLE_MUTATION_CHANCE = 0.03;
	private static final double RAMP_FACTOR = 1;
	private static final double WEAK_RAMP_MUTATION_CHANCE = 0.02;
	private static final double STRUCTURAL_RAMP_MUTATION_CHANCE = 0.01;
	private ArrayList<ArrayList<Gene>> individuals = new ArrayList<ArrayList<Gene>>();
	private int generation = 0;
	
	private ArrayList<Gene> best; 
	
	private Random r = new Random();
	private int brainSize;
	
	private GeneFactory geneFactory;
	
	public GeneticPopulation(GeneFactory gf, int size, int brainSize) {
		individuals = new ArrayList<>();
		geneFactory = gf;
		this.brainSize = brainSize;
		setSize(size);
	}
	
	public void expandBrainSize(int count) {
		for(ArrayList<Gene> individual: individuals) {
			individualAppendGenes(individual, count);
		}
	}
	
	
	public void setSize(int n) {
		for(;individuals.size()<n;) {
			ArrayList<Gene> individual = new ArrayList<Gene>();
			individualAppendGenes(individual, brainSize);
			individuals.add(individual);
		}
	}
	
	protected void individualAppendGenes(ArrayList<Gene> individual, int count) {
		for (int i = 0; i<count; i++) {
			individual.add(geneFactory.buildGene());
		}
	}
	
	public ArrayList<ArrayList<Gene>> getGenomes() {
		return individuals;
	}
	
	@SuppressWarnings("unchecked")
	public void evolve(SimulationResults results) {
		double totalFitness = getTotalFitness(results);
		ArrayList<ArrayList<Gene>> nextGen = new ArrayList<>();
		for (; nextGen.size()<individuals.size()-1;) {
			double selectedFitness = r.nextDouble() * totalFitness;
			for (ArrayList<Gene> individual: individuals) {
				selectedFitness -= results.getFitness(individual);
				if (selectedFitness<0) {
					nextGen.add(generateBaby(individual));
					break;
				}
			}
		}
		best = (ArrayList<Gene>)best.clone();
		nextGen.add(best);
		individuals = nextGen;
		generation++;
	}
	
	private double getTotalFitness(SimulationResults results) {
		double total = 0;
		double max = -100000.0;
		double fit;
		for (ArrayList<Gene> individual: individuals) {
			fit = results.getFitness(individual);
			total += fit;
			if (fit>max) {
				best = individual;
				max = fit;
			}
		}
		return total;
	}
	
	private ArrayList<Gene> generateBaby(ArrayList<Gene> parent) {
		@SuppressWarnings("unchecked")
		ArrayList<Gene> baby = (ArrayList<Gene>) parent.clone();
		for (int i = 0; i<baby.size(); i++) {
			double ra = r.nextDouble();
			if (ra < STRUCTURAL_STABLE_MUTATION_CHANCE) {
				baby.set(i, geneFactory.buildGene());
			} else if (ra - STRUCTURAL_STABLE_MUTATION_CHANCE < WEAK_STABLE_MUTATION_CHANCE) {
				baby.set(i, baby.get(i).mutate());
			}
		}
		for (int i = 0; i<baby.size(); i++) {
			double ra = r.nextDouble() * Math.pow((baby.size() / (i+1)), RAMP_FACTOR);
			if (ra < STRUCTURAL_RAMP_MUTATION_CHANCE) {
				baby.set(i, geneFactory.buildGene());
			} else if (ra - STRUCTURAL_RAMP_MUTATION_CHANCE < WEAK_RAMP_MUTATION_CHANCE) {
				baby.set(i, baby.get(i).mutate());
			}
		}
		return baby;
	}
	
	public ArrayList<Gene> getBest() {
		return best;
	}

	public int getGeneration() {
		return generation;
	}
	
}
