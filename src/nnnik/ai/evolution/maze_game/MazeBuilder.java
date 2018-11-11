package nnnik.ai.evolution.maze_game;

import nnnik.maths.geometry.Vector2;

public class MazeBuilder {
	private Maze maze = new Maze();
	private Spawn spawn = null;
	private Goal goal = null;
	
	public Blockade addBlockade(Vector2 position, Vector2 size) {
		Blockade blockade = new Blockade(position, size);
		maze.getChildren().add(blockade);
		return blockade;
	}
	
	public Spawn setSpawn(Vector2 position) {
		if (spawn != null) {
			maze.getChildren().remove(spawn);
			maze.getContenders().clear();
		}
		spawn = new Spawn(position);
		maze.getChildren().add(spawn);
		return spawn;
	}
	
	public Goal setGoal(Vector2 position) {
		if (goal != null) {
			maze.getChildren().remove(goal);
		}
		goal = new Goal(position);
		maze.getChildren().add(goal);
		return goal;
	}
	
	public MazeContender addContender() {
		if (spawn == null) {
			new IllegalStateException("Contender added before the spawn was set");
		}
		MazeContender contender = new MazeContender(spawn);
		maze.getContenders().add(contender);
		return contender;
	}
	
	public Maze getMaze() {
		return maze;
	}
}
