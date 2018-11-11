package nnnik.ai.evolution.maze_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nnnik.ai.evolution.vector_steps_1.DOTS_UI;
import nnnik.maths.geometry.Vector2;

public class MazeContender extends MazeObject {
	
	private Color highlight = null;
	
	private InputProvider inputProvider;
	protected Spawn spawn;
	
	private boolean alive = true;
	private boolean active = true;
	private boolean unfinished = true;
	
	protected double timeAlive;
	
	protected Vector2 speed = new Vector2(0.0, 0.0);
	protected Vector2 acceleration = null;
	
	private static final double ACCELERATION_MULTIPLIER = 0.0005;
	private static final double SPEED_MULTIPLIER = 0.01;
	private static final double BREAK_FACTOR = -0.25;
	
	static final Vector2 size = new Vector2(0.1, 0.1);
	
	public MazeContender(Spawn spawnpoint, InputProvider ip) {
		super(spawnpoint.getPosition().clone());
		spawn = spawnpoint;
		inputProvider = ip;
	}
	
	@Override
	public Vector2 getSize() {
		return size;
	}
	
	@Override
	public Hitbox getHitbox() {
		return Hitbox.ELLIPSE;
	}
	
	public void move(double timeMultiplier) {
		if (active) {
			timeAlive += timeMultiplier;
			Vector2 deltaSpeed = acceleration.clone();
			deltaSpeed.multiply(timeMultiplier);
			speed.multiply(1-(ACCELERATION_MULTIPLIER*timeMultiplier*BREAK_FACTOR));
			speed.add(deltaSpeed);
			Vector2 deltaPos = speed.clone();
			deltaPos.multiply(timeMultiplier * SPEED_MULTIPLIER);
			position.add(deltaPos);
		}
	}
	
	public void updateAcceleration() {
		if (active) {
			acceleration = inputProvider.generateInput(this);
			if (acceleration == null) {
				deactivate();
				return;
			}
			acceleration = acceleration.clone();
			if (acceleration.magnitude()>1.0) {
				acceleration.normalize();
			}
			acceleration.multiply(ACCELERATION_MULTIPLIER);
			DOTS_UI.log(acceleration.getX()+" "+acceleration.getY());
		}
	}
	
	public void setHighlightColor(Color highlight) {
		this.highlight = highlight;
	}
	
	public void setInputProvider(InputProvider ip) {
		inputProvider = ip;
	}
	
	public InputProvider getInputProvider() {
		return inputProvider;
	}
	
	public double getTimeAlive() {
		return timeAlive;
	}
	
	public void die() {
		alive = false;
		deactivate();
	}
	
	public void win() {
		unfinished = false;
		deactivate();
	}
	
	public void deactivate() {
		active = false;
	}
	
	public boolean isUnfinished() {
		return unfinished;
	}
	
	public boolean isAlive() {
		return alive;
	}
	
	public boolean isActive() {
		return active;
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		double px = position.getX();
		double py = position.getY();
		double sx = size.getX();
		double sy = size.getY();
		if (highlight != null) {
			gc.setFill(highlight);
			gc.fillOval(px-sx, py-sy, 2*sx, 2*sy);
			sx = sx/2;
			sy = sy/2;
		}
		if (active) {
			gc.setFill(Color.BLUE);
		} else if (!alive) {
			gc.setFill(Color.RED);
		} else if (!unfinished) {
			gc.setFill(Color.AQUAMARINE);
		} else {
			gc.setFill(Color.ORANGE);
		}
		gc.fillOval(px-sx, py-sy, 2*sx, 2*sy);
	}
	
	public void reset() {
		unfinished = true;
		active = true;
		alive = true;
		position.multiply(0);
		position.add(spawn.getPosition());
		speed.multiply(0);
		acceleration = null;
		timeAlive = 0;
	}
	
}
