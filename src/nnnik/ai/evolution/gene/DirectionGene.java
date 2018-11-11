package nnnik.ai.evolution.gene;

import java.util.Random;

import nnnik.maths.geometry.Vector2;

public class DirectionGene extends Vector2 implements Gene {
	

	private static Random r = null;
	
	public DirectionGene(double x, double y) {
		super(x,y);
	}

	@Override
	public Gene mutate() {
		return randomDirection();
	}

	public static DirectionGene randomDirection() {
		if (r == null) {
			r = new Random();
		}
		double x = r.nextDouble()*10-5;
		double y = r.nextDouble()*10-5;
		DirectionGene v = new DirectionGene(x, y);
		v.normalize();
		return v;
	}
}
