package nnnik.ai.evolution.maze_game;

import javafx.scene.canvas.GraphicsContext;
import nnnik.maths.geometry.Vector2;

public abstract class MazeObject {
	
	protected Vector2 position; 
	
	public MazeObject(Vector2 position) {
		this.position = position;
	}
	
	public Vector2 getPosition() {
		return position;
	}
	
	public abstract Vector2 getSize();
	public abstract Hitbox getHitbox();

	public boolean collidesWith(MazeObject object2) {
		Hitbox hb1 = getHitbox();
		Hitbox hb2 = object2.getHitbox();
		Vector2 s1 = getSize();
		Vector2 p1 = getPosition();
		Vector2 s2 = object2.getSize();
		Vector2 p2 = object2.getPosition();
		while (true) {
			switch (hb1) {
				case NONE:
					return false;
				case RECTANGLE:
					switch (hb2) {
						case NONE:
							return false;
						case RECTANGLE:
							Vector2 p = p1.clone();
							p.subtract(p2);
							p.abs();
							Vector2 s = s1.clone();
							s.add(s2);
							return (p.getX()<s.getX() && p.getY()<s.getY());
						case ELLIPSE:
							p = p2.clone();
							p.subtract(p1);
							p.abs();
							p.reduce(s1);
							p.divide(s2);
							return (p.magnitude() <= 1.0);
					}
				case ELLIPSE:
					switch (hb2) {
						case NONE:
							return false;
						case RECTANGLE:
							Object tmp = hb1;
							hb1 = hb2;
							hb2 = (Hitbox)tmp;
							tmp = p1;
							p1 = p2;
							p2 = (Vector2) tmp;
							tmp = s1;
							s1 = s2;
							s2 = (Vector2) tmp;
							break;
						case ELLIPSE:
							Vector2 p = p1.clone();
							p.subtract(p2);
							Vector2 s = s1.clone();
							s.add(s2);
							p.divide(s);
							return (p.magnitude() <= 1.0);
					}
					break;
			}
		}
	}

	public void onContenderCollide(MazeContender contender) {}
	
	public void draw(GraphicsContext gc) {}
	
}
