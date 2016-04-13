package game.image;

public class RawImage implements Image {
	
	private int width;
	private int height;
	private byte[] rgba;
	
	public RawImage(int width, int height, byte[] rgba) {
		this.width = width;
		this.height = height;
		this.rgba = rgba;
	}

	@Override
	public int getWidth() {
		return width;
	}

	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public byte[] getRGBA() {
		return rgba;
	}

}
