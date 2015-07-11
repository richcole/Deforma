package art;

import game.GradientImage;
import game.Image;
import game.ImageResource;
import game.gl.GLResource;
import game.gl.GLTexture;

public class ImageTexture implements GLResource, TextureSupplier {

    private final Image image;

    private GLTexture tex;

    public ImageTexture(String path) {
        this.image = new ImageResource(path);
    }

    public ImageTexture(GradientImage image) {
        this.image = image;
    }

    public void init() {
		tex = new GLTexture().withImage(image);
	}
	
	public void dispose() {
	}

	public GLTexture getTexture() {
		return tex;
	}

}
