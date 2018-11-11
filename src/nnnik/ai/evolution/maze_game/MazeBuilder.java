package nnnik.ai.evolution.maze_game;

import nnnik.maths.geometry.Vector2;

public class MazeBuilder {
	private Maze maze = new Maze();
	private Spawn spawn = null;
	private Goal goal = null;
	
	public void addBlockade(Vector2 position, Vector2 size) {
		maze.getChildren().add(new Blockade(position, size));
	}
	
	public void setSpawn(Vector2 position) {
		if (spawn != null) {
			maze.getChildren().remove(spawn);
			maze.getContenders().clear();
		}
		spawn = new Spawn(position);
		maze.getChildren().add(spawn);
	}
	
	public void setGoal(Vector2 position) {
		if (goal != null) {
			maze.getChildren().remove(goal);
		}
		goal = new Goal(position);
		maze.getChildren().add(goal);
	}
	
	public void addContender(InputProvider ip) {
		if (spawn == null) {
			new IllegalStateException("Contender added befoe the spawn was set");
		}
		maze.getContenders().add(new MazeContender(spawn, ip));
	}
	
	public Maze getMaze() {
		return maze;
	}
}
