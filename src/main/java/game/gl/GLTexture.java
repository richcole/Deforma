package game.gl;

import game.events.EventBus;
import game.image.Image;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;

public class GLTexture extends GLResource {
	
	private int id;

	public GLTexture(EventBus eventBus) {
	  super(eventBus);
		this.id = GL11.glGenTextures();
	}

	protected Runnable dispose() {
		return () -> GL11.glDeleteTextures(id);
	}
	
	public void setImage(Image img) {
		Preconditions.checkNotNull(img);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, img.getWidth(), img.getHeight(), 0, 
                GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, img.getRGBA());
		GL30.glGenerateMipmap(id);
	  GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void bind(int i) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);		
	}

}
