package nnnik.ai.evolution.vector_steps_1;

import java.util.ArrayList;
import java.util.Random;

import nnnik.ai.evolution.gene.DirectionGene;
import nnnik.ai.evolution.gene.DistanceGene;
import nnnik.ai.evolution.gene.Gene;
import nnnik.ai.evolution.gene.GeneFactory;
import nnnik.ai.evolution.gene.SequenceGene;

public class ImplGeneFactory implements GeneFactory {
	
	private static final Random r = new Random();
	private static ArrayList<Gene> geneAlphabet;
	
	public ImplGeneFactory() {
		if (geneAlphabet == null) {
			geneAlphabet = new ArrayList<Gene>();
			geneAlphabet.add(new SequenceGene());
			geneAlphabet.add(DistanceGene.randomDistance());
			geneAlphabet.add(DirectionGene.randomDirection());
		}
	}
	
	@Override
	public Gene buildGene() {
		return geneAlphabet.get(r.nextInt(geneAlphabet.size())).mutate();
	}

}
