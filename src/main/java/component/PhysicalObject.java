package component;

import game.math.Matrix;
import game.math.Vector;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PhysicalObject {
	private double speed = 20;
	private double rotSpeed = 0.005;

	private double rx = 0, ry = 0;
	private Vector p = Vector.Z;
	private Vector fwd = Vector.FWD;
	private Vector right = Vector.RIGHT;
	private Vector up = Vector.UP;
	private Matrix rot = Matrix.IDENTITY;

	private Transform tr = Transform.IDENTITY;

	public void moveForward(double dt) {
		p = p.plus(fwd.times(dt * speed));
	}

	public void moveLeft(double dt) {
		p = p.minus(right.times(dt * speed));
	}

	public void moveBackward(double dt) {
		p = p.minus(fwd.times(dt * speed));
	}

	public void moveRight(double dt) {
		p = p.plus(right.times(dt * speed));
	}

	public void moveUp(double dt) {
		p = p.plus(up.times(dt * speed));
	}

	public void moveDown(double dt) {
		p = p.minus(up.times(dt * speed));
	}

	public void updateRotation(double drx, double dry) {
		rx += drx * rotSpeed;
		ry += dry * rotSpeed;
		Matrix rotX = Matrix.rot2(-rx,  Vector.UP);
		Matrix rotY = Matrix.rot2(ry,  rotX.times(Vector.RIGHT));
		rot = rotY.times(rotX);
		fwd = rot.times(Vector.FWD);
		up = rot.times(Vector.UP);
		right = rot.times(Vector.RIGHT);
	}

	public void updateTransform() {
		tr = new DefaultTransform(Matrix.translate(p).times(rot));
	}

	public Transform getLocalTransform() {
		return tr;
	}
}
