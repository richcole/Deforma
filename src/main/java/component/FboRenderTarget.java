package component;

import component.gl.GlEngine;
import game.gl.GLFrameBuffer;
import game.gl.GLRenderBuffer;
import game.gl.GLTexture;
import org.lwjgl.opengl.GL11;

public class FboRenderTarget extends DefaultComponent implements RenderTarget {

	private int width, height;
	private GLFrameBuffer frameBuffer;
	private GLRenderBuffer renderBuffer;
	private GLTexture texture;

	public FboRenderTarget(GlEngine engine) {
		width = 320;
		height = 200;
		frameBuffer = new GLFrameBuffer(engine.getDisposer());
		renderBuffer = new GLRenderBuffer(engine.getDisposer());
		texture = new GLTexture(engine.getDisposer());
		texture.configureForColorChannel(width, height, null);
		frameBuffer.bindTexture(texture);
		renderBuffer.bindFrameBuffer(frameBuffer, width, height);
	}

	@Override
	public void bind(GlEngine glEngine) {
		frameBuffer.bind(width, height);
	}

	@Override
	public void preRender(GlEngine glEngine) {
		GL11.glClearColor(0, 0, 0, 1); // black
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void postRender(GlEngine glEngine) {
	}

	public GLTexture getTexture() {
		return texture;
	}
}
