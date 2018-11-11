package nnnik.ai.evolution.maze_game;

import nnnik.maths.geometry.Vector2;

public class Spawn extends MazeObject {
	
	private static final Vector2 size = new Vector2(0.0, 0.0);
	
	public Spawn(Vector2 position) {
		super(position);
	}

	@Override
	public Vector2 getSize() {
		return size;
	}

	@Override
	public Hitbox getHitbox() {
		// TODO Auto-generated method stub
		return Hitbox.NONE;
	}

}
