package nnnik.ai.evolution.maze_game;

import nnnik.maths.geometry.Vector2;

public interface InputProvider {
	
	public Vector2 generateInput(MazeContender contender);
	
}
