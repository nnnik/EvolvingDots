package nnnik.ai.evolution.vector_steps_1;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Dot {
	
	private Vector2 acceleration = null;
	private Vector2 pos = null;
	private Vector2 speed = null;
	private Brain b = null;
	private boolean active = true;
	private boolean won = false;
	private boolean dead = false;
	private double fitness = 0;
	private int lastStep;
	static double accelerationMultiplier = 0.02;
	
	Dot(int size) {
		this(500.0, 850.0);
		b = new Brain();
		b.fillRandom(size);
	}
	
	Dot(Brain b) {
		this(500.0, 850.0);
		this.b = b; 
	}
	
	Dot(double x, double y) {
		pos = new Vector2(x, y);
		speed = new Vector2();
		acceleration = new Vector2();
	}
	
	public void draw(GraphicsContext gc) {
		if (active) {
			gc.setFill(Color.BLUE);
		} else if (dead) {
			gc.setFill(Color.RED);
		} else if (won) {
			gc.setFill(Color.AQUAMARINE);
		} else {
			gc.setFill(Color.ORANGE);
		}
		gc.fillOval(pos.getX(), pos.getY(), 8, 8);
	}
	
	public void think(int stepNumber) {
		if (active) {
			lastStep = stepNumber;
			acceleration = b.getNext(stepNumber);
			if (acceleration == null) {
				active = false;
			}
		}
	}
	public void calculatePhysics() {
		double x = pos.getX();
		double y = pos.getY();
		if (x<0||x>992||y<0||y>992) {
			die();
		} else if ((x<200||x>292) && y<375 && y>342) {
			die();
		} else if (x<500 && y>192 && y<225) {
			die();
		} else if (getDistanceToGoal()<24) {
			win();
		}
	}
	
	public void move(double timeMultiplier) {
		if (active) {
			Vector2 deltaSpeed = acceleration.clone();
			deltaSpeed.multiply(accelerationMultiplier);
			speed.add(deltaSpeed);
			if (speed.magnitude()>1.0) {
				speed.normalize();
			}
			Vector2 deltaPos = speed.clone();
			deltaPos.multiply(timeMultiplier);
			pos.add(deltaPos);
			calculatePhysics();
		}
	}
	
	public void die() {
		active = false;
		dead = true;
	}
	
	public void win() {
		active = false;
		won = true;
	}
	
	public Vector2 getPosition() {
		return pos;
	}
	
	private double getDistanceToGoal() {
		return Math.pow(Math.pow(pos.getX()-500,2)+Math.pow(pos.getY()-50,2), 0.5);
	}
	
	public double getFitness() {
		if (fitness == 0) {
			if (!won) {
				fitness = Math.pow(1/getDistanceToGoal(),8.0);
				if (dead) {
					fitness *= 0.7;
				}
			} else {
				fitness = 2.0-Math.pow((1-(double)(1.0/(double)lastStep)), 8.0);
			}
		}
		return fitness;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Dot generateBaby() {
		Brain babyBrain = b.clone();
		babyBrain.mutate();
		return new Dot(babyBrain);
	}
	
	public Dot clone() {
		return new Dot(b);
	}
	
	public Brain getBrain() {
		return b;
	}
	
	public boolean hasWon() {
		return won;
	}
}
