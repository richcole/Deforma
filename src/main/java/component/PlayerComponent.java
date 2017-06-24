package component;

import game.math.Matrix;
import game.math.Vector;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerComponent extends DefaultComponent {
	private static final Logger log = LoggerFactory.getLogger(PlayerComponent.class);

	private double speed = 20;
	private double rotSpeed = 0.005;

	private double rx = 0, ry = 0;
	private Vector p = Vector.Z;
	private Vector fwd = Vector.FWD;
	private Vector right = Vector.RIGHT;
	private Vector up = Vector.UP;
	private Matrix rot = Matrix.IDENTITY;

	public PlayerComponent() {
	}

	@Override
	public void update(Scene scene) {
		double dt = scene.getDeltaTime();
		InputController ic = scene.getInputController();
		boolean updated = false;
		if (ic.isKeyDown(Keyboard.KEY_W)) {
			p = p.plus(fwd.times(dt * speed));
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_A)) {
			p = p.plus(right.times(dt * speed));
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_S)) {
			p = p.minus(fwd.times(dt * speed));
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_D)) {
			p = p.minus(right.times(dt * speed));
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_Q)) {
			p = p.plus(up.times(dt * speed));
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_E)) {
			p = p.minus(up.times(dt * speed));
			updated = true;
		}
		if (ic.isMouseDown(0)) {
			rx += ic.getMdx() * rotSpeed;
			ry += ic.getMdy() * rotSpeed;
			updateRotation();
			updated = true;
		}
		if (updated) {
			setLocalTransform(new DefaultTransform(Matrix.translate(p).times(rot)));
		}
	}

	private void updateRotation() {
		Matrix rotX = Matrix.rot2(-rx,  Vector.UP);
		Matrix rotY = Matrix.rot2(ry,  rotX.times(Vector.RIGHT));
		rot = rotY.times(rotX);
		fwd = rot.times(Vector.FWD);
		up = rot.times(Vector.UP);
		right = rot.times(Vector.RIGHT);
	}
}
