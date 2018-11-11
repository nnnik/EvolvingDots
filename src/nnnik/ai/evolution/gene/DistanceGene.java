package nnnik.ai.evolution.gene;

import java.util.Random;

//will repeat the next genes until the specific distance has been traveled
//the specific distance uses the position where the individual first encountered this gene
//if the individual is further away than this distance the brain should jump to the next control gene
//or start from scratch if none other control genes are encountered

public class DistanceGene implements Gene {
	
	private double distance;
	private static Random r = null;
	
	public DistanceGene(double distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}
	
	@Override
	public Gene mutate() {
		// TODO Auto-generated method stub
		return randomDistance();
	}
	
	public static DistanceGene randomDistance() {
		if (r == null) {
			r = new Random();
		}
		return new DistanceGene(r.nextDouble()*2);
	}
	
}
