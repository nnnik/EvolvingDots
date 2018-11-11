package nnnik.ai.evolution.vector_steps_1;

import nnnik.ai.evolution.maze_game.InputProvider;
import nnnik.ai.evolution.maze_game.MazeContender;
import nnnik.maths.geometry.Vector2;

public class KeyboardBrain implements InputProvider {
	
	private boolean up = false, down = false, left = false, right = false;
	private Vector2 acceleration = new Vector2(0.0, 0.0);
	private static final Vector2 upVector    = new Vector2(0.0, -1.0), 
	                             downVector  = new Vector2(0.0, 1.0), 
	                             leftVector  = new Vector2(-1.0, 0.0), 
	                             rightVector = new Vector2(1.0, 0.0);
	
	public void setUpState(boolean state) {
		if (state^up) {
			if (state) {
				acceleration.add(upVector);
			} else {
				acceleration.subtract(upVector);
			}
			up = state;
		}
	}
	
	public void setDownState(boolean state) {
		if (state^down) {
			if (state) {
				acceleration.add(downVector);
			} else {
				acceleration.subtract(downVector);
			}
			down = state;
		}
	}
	
	public void setLeftState(boolean state) {
		if (state^left) {
			if (state) {
				acceleration.add(leftVector);
			} else {
				acceleration.subtract(leftVector);
			}
			left = state;
		}
	}
	
	public void setRightState(boolean state) {
		if (state^right) {
			if (state) {
				acceleration.add(rightVector);
			} else {
				acceleration.subtract(rightVector);
			}
			right = state;
		}
	}
	
	@Override
	public Vector2 generateInput(MazeContender contender) {
		// TODO Auto-generated method stub
		return acceleration;
	}

}
