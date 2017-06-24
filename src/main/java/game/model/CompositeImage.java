package game.model;

import game.gl.GLFactory;
import game.gl.GLTexture;

import java.util.List;

public interface CompositeImage {
	double[] transform(int nv, int[] i, List<String> imageList, double t[]);

	GLTexture getTexture(GLFactory glFactory);
}
