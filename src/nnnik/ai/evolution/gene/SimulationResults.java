package nnnik.ai.evolution.gene;

import java.util.List;

public interface SimulationResults {
	public double getFitness(List<Gene> individual);
}
