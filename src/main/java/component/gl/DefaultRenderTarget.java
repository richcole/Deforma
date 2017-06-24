package component.gl;

import component.DefaultComponent;
import component.RenderTarget;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class DefaultRenderTarget extends DefaultComponent implements RenderTarget {

	@Override
	public void preRender(GlEngine glEngine) {
		if (Display.wasResized()) {
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}

		GL11.glClearColor(0, 0, 0, 1); // black
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}

	@Override
	public void bind(GlEngine engine) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}


	@Override
	public void postRender(GlEngine glEngine) {
		Display.update();
	}
}
