package game;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.math.Vector;

public class PositionController implements Simulant, InputController {
	
	
	private static final Logger log = LoggerFactory.getLogger(PositionController.class);
	
	private View   view;
	private Vector vel;
	private float  scale = 0.01f;

	PositionController(View view) {
		this.view = view;
		this.vel = Vector.Z;
	}
	
	public void tick(long dt) {
		if ( ! vel.equals(Vector.Z) ) {
			view.move(vel.times(dt/100.0));
		}
	}

	public void mouseMove(int i, int j) {
	}

	public void keyDown(int eventKey) {
		if ( eventKey == Keyboard.KEY_A) {
			vel = vel.minus(Vector.U1);
		}
		if ( eventKey == Keyboard.KEY_D) {
			vel = vel.plus(Vector.U1);
		}
		if ( eventKey == Keyboard.KEY_W) {
			vel = vel.minus(Vector.U3);
		}
		if ( eventKey == Keyboard.KEY_S) {
			vel = vel.plus(Vector.U3);
		}
	}

	public void keyUp(int eventKey) {
		if ( eventKey == Keyboard.KEY_A) {
			vel = vel.plus(Vector.U1);
		}
		if ( eventKey == Keyboard.KEY_D) {
			vel = vel.minus(Vector.U1);
		}
		if ( eventKey == Keyboard.KEY_W) {
			vel = vel.plus(Vector.U3);
		}
		if ( eventKey == Keyboard.KEY_S) {
			vel = vel.minus(Vector.U3);
		}
	}

}
