package game.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GLRenderBuffer extends GLResource {

	final static Logger log = LoggerFactory.getLogger(GLRenderBuffer.class);

	int id = -1;

	public GLRenderBuffer(Disposer disposer) {
		super(disposer);
		id = GL30.glGenRenderbuffers();
	}

	protected Runnable dispose() {
		return () -> GL30.glDeleteFramebuffers(id);
	}

	public int getId() {
		return id;
	}

	public GLRenderBuffer bindFrameBuffer(GLFrameBuffer frameBuffer, int width, int height) {
		GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, id);
		GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);
		GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, id);
		return this;
	}
	
}
