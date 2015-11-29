package game;

import java.io.File;

import com.google.common.base.Preconditions;

public class ImageTexture extends Material {

	private GLTexture tex;
	private Image image;
	
	public ImageTexture(Image image) {
		Preconditions.checkNotNull(image);
		this.image = image;
	}
	
	public ImageTexture(String path) {
		Preconditions.checkNotNull(path);
		this.image = new ImageResource(path);
	}

	public ImageTexture(File file) {
		Preconditions.checkNotNull(file);
		this.image = new ImageResource(file);
	}

	@Override
	public void init() {
    this.tex = new GLTexture().withImage(image);
	}

	@Override
	public void dispose() {
	}
	
	public GLTexture getTexture() {
		ensureInitialized();
		return tex;
	}
}
