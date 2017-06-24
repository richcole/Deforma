package game.gl;

import game.image.Image;
import game.math.Utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;

import java.nio.ByteBuffer;

public class GLTexture extends GLResource {

	private int id;

	public GLTexture(Disposer disposer) {
		super(disposer);
		this.id = GL11.glGenTextures();
	}

	public GLTexture(Disposer disposer, Image image) {
		this(disposer);
		setImage(image);
	}

	protected Runnable dispose() {
		return () -> GL11.glDeleteTextures(id);
	}

	public void setImage(Image img) {
		Preconditions.checkNotNull(img);
		configureForColorChannel(img.getWidth(), img.getHeight(), Utils.toByteBuffer(img.getRGBA()));
		GL30.glGenerateMipmap(id);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}

	public void updateImage(Image img) {
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, img.getWidth(), img.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE,  Utils.toByteBuffer(img.getRGBA()));
	}

	public void configureForColorChannel(int width, int height, ByteBuffer imageBuffer) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, imageBuffer);
	}

	public void configureForDepthChannel(int width, int height) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null);
	}

	public void bind(int i) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0 + i);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
	}

	public int getId() {
		return id;
	}
}
