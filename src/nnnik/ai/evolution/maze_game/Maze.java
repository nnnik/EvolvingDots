package nnnik.ai.evolution.maze_game;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import nnnik.ai.evolution.vector_steps_1.DOTS_UI;
import nnnik.maths.geometry.Vector2;

@SuppressWarnings("unused")
public class Maze {
	
	private static final Vector2 size = new Vector2(10.0,10.0);
	
	private List<MazeObject> children = new ArrayList<MazeObject>();
	private List<MazeContender> contenders = new ArrayList<MazeContender>();
	
	public List<MazeObject> getChildren() {
		return children;
	}
	
	public List<MazeContender> getContenders() {
		return contenders;
	}
	
	public void update(double timeSinceLastUpdate) {
		for (MazeContender contender: contenders) {
			if (contender.isActive()) {	
				contender.updateAcceleration();
				contender.move(timeSinceLastUpdate);
				detectCollisions(contender);
			}
		}
	}
	
	private void detectCollisions(MazeContender contender) {
		if (Math.abs(contender.getPosition().getX())+contender.getSize().getX() > this.getSize().getX()
				|| Math.abs(contender.getPosition().getY())+contender.getSize().getY() > this.getSize().getY()) {
			contender.die();
		}
		for (MazeObject child: children) {
			if (child.collidesWith(contender)) {
				child.onContenderCollide(contender);
			}
		}
	}
	
	public Vector2 getSize() {
		return size;
	}
	
	public void draw(GraphicsContext gc) {
		double x = this.getSize().getX();
		double y = this.getSize().getY();
		gc.clearRect(-x, -y, 2*x, 2*y);
		for (MazeContender contender: contenders) {
			contender.draw(gc);
		}
		for (MazeObject child: children) {
			child.draw(gc);
		}
	}
	
	public void reset() {
		for (MazeContender contender: contenders) {
			contender.reset();
		}
	}
	
	public boolean allInactive() {
		for (MazeContender contender: contenders) {
			if (contender.isActive()) {
				return false;
			}
		}
		return true;
	}
	
}
