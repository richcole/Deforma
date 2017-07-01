package component;

import game.math.Matrix;
import game.math.Vector;

public interface Transform {
	public static final Transform IDENTITY = new DefaultTransform(Matrix.IDENTITY, Matrix.IDENTITY);

	Matrix getTr();
	Matrix getInvTr();

	default Transform transform(Transform childTransform) {
		if (childTransform == Transform.IDENTITY) {
			return this;
		}
		else {
			return new DefaultTransform(getTr().times(childTransform.getTr()));
		}
	}

	static Transform at(double x, double y, double z) {
		return new DefaultTransform(Matrix.translate(new Vector(x, y, z, 1.0)));
	}

	Transform translate(Vector x);

	Transform invert();

	static Transform lookAt(Vector eye, Vector target, Vector up) {
		Vector zAxis = target.minus(eye).normalize();
		Vector xAxis = up.cross(zAxis).normalize();
		Vector yAxis = zAxis.cross(xAxis);

		Matrix m = new Matrix();
		m.set(0, 0, xAxis.x());
		m.set(0, 1, xAxis.y());
		m.set(0, 2, xAxis.z());
		m.set(0, 3, -xAxis.dot(eye));
		m.set(1, 0, yAxis.x());
		m.set(1, 1, yAxis.y());
		m.set(1, 2, yAxis.z());
		m.set(1, 3, -yAxis.dot(eye));
		m.set(2, 0, zAxis.x());
		m.set(2, 1, zAxis.y());
		m.set(2, 2, zAxis.z());
		m.set(2, 3, -zAxis.dot(eye));
		m.set(3, 0, 0);
		m.set(3, 1, 0);
		m.set(3, 2, 0);
		m.set(3, 3, 1);

		return new DefaultTransform(m);
	}
}
