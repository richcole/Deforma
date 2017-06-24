package game.gl;

import game.model.UniformBindingPool.UniformBinding;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.GL32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class GLFrameBuffer extends GLResource {

	final static Logger log = LoggerFactory.getLogger(GLFrameBuffer.class);

	int id = -1;

	public GLFrameBuffer(Disposer disposer) {
		super(disposer);
		id = GL30.glGenFramebuffers();
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);
	}

	protected Runnable dispose() {
		return () -> GL30.glDeleteFramebuffers(id);
	}

	public int getId() {
		return id;
	}

	public GLFrameBuffer bindTexture(GLTexture texture) {
		GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture.getId(), 0);
		return this;
	}
	
	public GLFrameBuffer bind(int width, int height) {
		GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, id);
		GL11.glViewport(0, 0, width, height);
		return this;
	}
}
