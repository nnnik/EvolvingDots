package nnnik.ai.evolution.maze_game;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import nnnik.maths.geometry.Vector2;

public class Blockade extends MazeObject {
	
	private Vector2 size;
	
	public Blockade(Vector2 position, Vector2 size) {
		super(position);
		this.size = size;
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	@Override
	public Hitbox getHitbox() {
		return Hitbox.RECTANGLE;
	}
	
	@Override
	public void onContenderCollide(MazeContender contender) {
		contender.die();
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		double px = position.getX();
		double py = position.getY();
		double sx = size.getX();
		double sy = size.getY();
		gc.setFill(Color.BLACK);
		gc.fillRect(px-sx, py-sy, 2*sx, 2*sy);
	}
	
}
