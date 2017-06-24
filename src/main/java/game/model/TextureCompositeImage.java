package game.model;

import game.gl.GLFactory;
import game.gl.GLTexture;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class TextureCompositeImage implements CompositeImage {
	private final GLTexture texture;

	@Override
	public double[] transform(int nv, int[] i, List<String> imageList, double[] t) {
		return t;
	}

	@Override
	public GLTexture getTexture(GLFactory glFactory) {
		return texture;
	}
}
