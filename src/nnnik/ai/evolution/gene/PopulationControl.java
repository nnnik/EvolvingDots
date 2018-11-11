package nnnik.ai.evolution.gene;

public interface PopulationControl {
	public GenomeContainer createIndividual();
	public void destroyIndividual(GenomeContainer individual);
}
