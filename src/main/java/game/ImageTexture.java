package game;

import java.io.File;

import com.google.common.base.Preconditions;

public class ImageTexture extends Material {

	private GLTexture tex;
	
	public ImageTexture(Image image) {
		Preconditions.checkNotNull(image);
    this.tex = new GLTexture().withImage(image);
	}
	
	public ImageTexture(String path) {
		this(new ImageResource(path));
	}

	public ImageTexture(File file) {
	  this(new ImageResource(file));
	}

	public GLTexture getTexture() {
		return tex;
	}
}
