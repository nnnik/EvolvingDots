package nnnik.maths.geometry;

public class Vector2 {
	
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
	
	public void abs() {
		x = Math.abs(x);
		y = Math.abs(y);
	}
	
	public void reduce(Vector2 v2) {
		subtract(v2);
		if (x < 0) {
			x = 0;
		}
		if (y < 0) {
			y = 0;
		}
	}
	
	public double distance(Vector2 v2) {
		return Math.pow(Math.pow(this.x-v2.x,2)+Math.pow(this.y-v2.y,2), 0.5);
	}
	
	public double cabDistance(Vector2 v2) {
		return Math.abs(x-v2.x+y-v2.y);
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	@Override
	public Vector2 clone() {
		return new Vector2(x,y);
	}

	public void divide(Vector2 v2) {
		x = x /	v2.getX();
		y = y / v2.getY();
	}
	
}
