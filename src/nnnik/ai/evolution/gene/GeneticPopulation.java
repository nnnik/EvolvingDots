package nnnik.ai.evolution.gene;

import java.util.Random;
import java.util.ArrayList;

public class GeneticPopulation {
	
	
	private static final Random r = new Random();
	
	private static final double STRUCTURAL_STABLE_MUTATION_CHANCE = 0.0;
	private static final double WEAK_STABLE_MUTATION_CHANCE = 0.01;
	private static final double RAMP_FACTOR = 2;
	private static final double WEAK_RAMP_MUTATION_CHANCE = 0.05;
	private static final double STRUCTURAL_RAMP_MUTATION_CHANCE = 0.01;
	
	private ArrayList<GenomeContainer> individuals = new ArrayList<GenomeContainer>();
	private int generation = 0;
	
	private GenomeContainer best; 

	private int brainSize;
	
	private GeneFactory geneFactory;
	private PopulationControl populationControl;
	
	public GeneticPopulation(GeneFactory gf, PopulationControl pc, int size, int brainSize) {
		individuals = new ArrayList<>();
		geneFactory = gf;
		this.brainSize = brainSize;
		populationControl = pc;
		setSize(size);
	}
	
	public void expandBrainSize(int count) {
		for(GenomeContainer individual: individuals) {
			individualAppendGenes(individual, count);
		}
	}
	
	
	public void setSize(int size) {
		for(;individuals.size()<size;) {
			GenomeContainer individual = populationControl.createIndividual();
			individualAppendGenes(individual, brainSize);
			individuals.add(individual);
		}
	}
	
	protected void individualAppendGenes(GenomeContainer individual, int count) {
		ArrayList<Gene> individualGenome = individual.getGenome();
		if (individualGenome == null) {
			individualGenome = new ArrayList<Gene>();
			individual.setGenome(individualGenome);
		}
		for (int i = 0; i<count; i++) {
			individualGenome.add(geneFactory.buildGene());
		}
	}
	
	public ArrayList<GenomeContainer> getPopulation() {
		return individuals;
	}
	
	public void evolve(SimulationResults results) {
		double totalFitness = getTotalFitness(results);
		ArrayList<ArrayList<Gene>> nextGen = new ArrayList<>();
		for (; nextGen.size()<individuals.size()-1;) {
			double selectedFitness = r.nextDouble() * totalFitness;
			for (GenomeContainer individual: individuals) {
				selectedFitness -= results.getFitness(individual);
				if (selectedFitness<0) {
					nextGen.add(generateBaby(individual.getGenome()));
					break;
				}
			}
		}
		for (int i=0, j=0; j<individuals.size(); i++, j++) {
			GenomeContainer individual = individuals.get(j);
			if (individual != best) {
				individual.setGenome(nextGen.get(i));
			} else {
				i--;
				individual.setGenome(individual.getGenome());
			}
		}
		generation++;
	}
	
	private double getTotalFitness(SimulationResults results) {
		double total = 0;
		double max = -100000.0;
		double fit;
		for (GenomeContainer individual: individuals) {
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
	
	public GenomeContainer getBest() {
		return best;
	}

	public int getGeneration() {
		return generation;
	}
	
}
