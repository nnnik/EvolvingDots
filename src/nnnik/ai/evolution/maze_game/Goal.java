package nnnik.ai.evolution.maze_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nnnik.maths.geometry.Vector2;

public class Goal extends MazeObject {
	
	private static final Vector2 size = new Vector2(0.4, 0.4);
	
	public Goal(Vector2 position) {
		super(position);
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	@Override
	public Hitbox getHitbox() {
		return Hitbox.ELLIPSE;
	}
	
	@Override
	public void onContenderCollide(MazeContender contender) {
		contender.win();
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		double px = position.getX();
		double py = position.getY();
		double sx = size.getX();
		double sy = size.getY();
		gc.setFill(Color.GOLD);
		gc.fillOval(px-sx, py-sy, 2*sx, 2*sy);
	}

}
