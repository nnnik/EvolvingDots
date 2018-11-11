package nnnik.ai.evolution.gene;

public class SequenceGene implements Gene {

	@Override
	public Gene mutate() {
		return new SequenceGene();
	}

}
