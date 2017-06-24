package component;

import game.math.Matrix;
import game.math.Vector;

public class DefaultTransform implements Transform {
	private Matrix tr;

	public DefaultTransform(Matrix tr) {
		this.tr = tr;
	}

	@Override
	public Matrix getTr() {
		return tr;
	}

	public Transform translate(Vector x) {
		return new DefaultTransform(tr.times(Matrix.translate(x)));
	}

	@Override
	public Transform invert() {
		return new DefaultTransform(getTr().invert());
	}

}
