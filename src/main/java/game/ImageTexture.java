package game;

import game.events.EventBus;

import java.io.File;

import com.google.common.base.Preconditions;

public class ImageTexture extends Material {

	private GLTexture tex;
	
	public ImageTexture(EventBus eventBus, Image image) {
		Preconditions.checkNotNull(image);
    this.tex = new GLTexture(eventBus).withImage(image);
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
