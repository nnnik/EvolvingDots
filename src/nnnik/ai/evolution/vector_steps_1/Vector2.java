package nnnik.ai.evolution.vector_steps_1;

import java.util.Random;

public class Vector2 {
	private static Random r = null;
	
	private double x;
	private double y;
	
	public Vector2(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
	public Vector2() {
		this(0.0, 0.0);
	}
	
	public void add(Vector2 v2) {
		this.x += v2.x;
		this.y += v2.y;
	}
	
	public void subtract(Vector2 v2) {
		this.x -= v2.x;
		this.y -= v2.y;
	}
	
	public void multiply(double n) {
		this.x *= n;
		this.y *= n;
	}
	
	public double magnitude() {
		return Math.pow(Math.pow(x,2)+Math.pow(y,2), 0.5);
	}
	
	public void normalize() {
		double m = magnitude();
		if (m>0) {
			x = x/m;
			y = y/m;
		}
	}
	
	public double distance(Vector2 v2) {
		return Math.pow(Math.pow(this.x-v2.x,2)+Math.pow(this.y-v2.y,2), 0.5);
	}
	
	public double cabDistance(Vector2 v2) {
		return x-v2.x+y-v2.y;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public Vector2 clone() {
		return new Vector2(x,y);
	}
	
	static public Vector2 randomMovement() {
		if (r == null) {
			r = new Random();
		}
		double x = r.nextDouble()*2-1;
		double y = r.nextDouble()*2-1;
		Vector2 v = new Vector2(x, y);
		v.normalize();
		return v;
	}
	
}
