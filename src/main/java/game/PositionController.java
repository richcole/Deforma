package game;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionController implements Simulant, InputController {
	
	
	private static final Logger log = LoggerFactory.getLogger(PositionController.class);
	
	private View   view;
	
	private double rx, ry;
	private double vx, vy;
	
	private boolean mouseDown = false;

	Matrix rot1 = Matrix.IDENTITY;
	Matrix rot2 = Matrix.IDENTITY;

	PositionController(View view) {
		this.view = view;
		this.rx = 0;
		this.ry = 0;
	}
	
	public void tick(long dt) {
		if ( vx != 0 || vy != 0 ) {
			Vector dp = Vector.U1.times(vx / dt).plus(Vector.U3.times(vy / dt));
			Vector rdp = rot2.times(dp);
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
		double sx = rx / 360.0;
		double sy = ry / 360.0;
		
		Matrix rotX1 = Matrix.rot2(sx, Vector.U2);
		Matrix rotX2 = Matrix.rot2(-sx, Vector.U2);
		
		Matrix rotY1 = Matrix.rot2(sy, Vector.U1);
		Matrix rotY2 = Matrix.rot2(-sy, Vector.U1);
		
		rot1 = rotY1.times(rotX1);
		rot2 = rotX2.times(rotY2);
		
		view.setRotation(rot1);
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
	
	public Vector getForward() {
		return rot2.times(Vector.U3);
	}
	
	public Vector getPosition() {
		return view.getPosition();
	}

}
