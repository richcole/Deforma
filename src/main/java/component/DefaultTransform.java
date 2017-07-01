package component;

import game.math.Matrix;
import game.math.Vector;
import lombok.Getter;

public class DefaultTransform implements Transform {

	@Getter
	private Matrix tr;

	@Getter
	private Matrix invTr;

	public DefaultTransform(Matrix tr) {
		this.tr = tr;
		this.invTr = tr.invert();
	}

	public DefaultTransform(Matrix tr, Matrix invTr) {
		this.tr = tr;
		this.invTr = invTr;
	}

	public Transform translate(Vector x) {
		return new DefaultTransform(tr.times(Matrix.translate(x)), Matrix.translate(x.minus()).times(tr));
	}

	@Override
	public Transform invert() {
		return new DefaultTransform(invTr, tr);
	}

}
