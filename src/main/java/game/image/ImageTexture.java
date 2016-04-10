package game.image;

import game.events.EventBus;
import game.gl.GLTexture;

import java.io.File;

import com.google.common.base.Preconditions;

public class ImageTexture implements Material {

	private GLTexture tex;
	
	public ImageTexture(EventBus eventBus, Image image) {
		Preconditions.checkNotNull(image);
    this.tex = new GLTexture(eventBus);
    this.tex.setImage(image);
	}
	
	public ImageTexture(EventBus eventBus, String path) {
		this(eventBus, new ImageResource(path));
	}

	public ImageTexture(EventBus eventBus, File file) {
	  this(eventBus, new ImageResource(file));
	}

	public GLTexture getTexture() {
		return tex;
	}
}
