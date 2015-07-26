package game;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.math.Quaternion;
import game.math.Vector;

public class PositionController implements Simulant, InputController {
	
	
	private static final Logger log = LoggerFactory.getLogger(PositionController.class);
	
	private View   view;
	
	private double rx, ry;
	private double vx, vy;
	
	private boolean mouseDown = false;

	Quaternion rotation = Quaternion.ZERO;

	PositionController(View view) {
		this.view = view;
		this.rx = 0;
		this.ry = 0;
	}
	
	public void tick(long dt) {
		if ( vx != 0 || vy != 0 ) {
			Vector dp = Vector.U1.times(vx / dt).plus(Vector.U3.times(vy / dt));
			Vector rdp = rotation.conjugate().rotate(dp);
			view.move(rdp);
		}
	}

	public void mouseMove(int dx, int dy) {
		if ( mouseDown ) {
			rotate(dx, dy);
		}
	}
	
	private void rotate(int drx, int dry) {
		rx += drx;
		ry += dry;
		double sx = Math.sin(rx / 360.0);
		double sy = Math.sin(ry / 360.0);
		rotation = new Quaternion(0, sx, 0, Math.sqrt(1 - sx*sx))
		    .times(new Quaternion(sy, 0, 0, Math.sqrt(1 - sy*sy)));
		view.setRotation(rotation);
	}

	public void mouseDown(int button) {
		if ( button == 0 ) {
			mouseDown = true;
		}
	}

	public void mouseUp(int button) {
		if ( button == 0 ) {
			mouseDown = false;
		}
	}

	public void keyDown(int eventKey) {
		if ( eventKey == Keyboard.KEY_A) {
			vx = -1;
		}
		if ( eventKey == Keyboard.KEY_D) {
			vx = 1;
		}
		if ( eventKey == Keyboard.KEY_W) {
			vy = -1;
		}
		if ( eventKey == Keyboard.KEY_S) {
			vy = 1;
		}
	}

	public void keyUp(int eventKey) {
		if ( eventKey == Keyboard.KEY_A) {
			vx = 0;
		}
		if ( eventKey == Keyboard.KEY_D) {
			vx = 0;
		}
		if ( eventKey == Keyboard.KEY_W) {
			vy = 0;
		}
		if ( eventKey == Keyboard.KEY_S) {
			vy = 0;
		}
	}

}
