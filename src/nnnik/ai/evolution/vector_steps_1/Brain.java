package nnnik.ai.evolution.vector_steps_1;

import java.util.*;

public class Brain {
	private ArrayList<Vector2> steps = null;
	private static Random r = null;
	
	public Brain() {
		steps = new ArrayList<Vector2>();
	}
	
	public Brain(ArrayList<Vector2> steps) {
		this.steps = steps;
	}
	
	public void addStep(Vector2 newStep) {
		steps.add(newStep);
	}
	
	public int getStepCount() {
		return steps.size();
	}
	
	public Vector2 getNext(int index) {
		if (index < getStepCount()) {
			return steps.get(index);
		}
		return null;
	}
	
	public void fillRandom(int n) {
		for (int i = 0; i<n; i++) {
			Vector2 v = Vector2.randomMovement();
			steps.add(v);
		}
	}
	
	@SuppressWarnings("unchecked")
	public Brain clone() {
		return new Brain((ArrayList<Vector2>)steps.clone());
	}
	
	public void mutate() {
		if (r==null) {
			r = new Random();
		}
		for (int i=0; i<steps.size(); i++) {
			if (r.nextDouble()<0.05) {
				steps.set(i, Vector2.randomMovement());
			}
			if (r.nextDouble()<(0.1*(i/steps.size()))) {
				steps.set(i, Vector2.randomMovement());
			}
		}
	}
}
