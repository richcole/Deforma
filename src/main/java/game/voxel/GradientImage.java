package game.voxel;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class GradientImage implements Image {

	private ByteBuffer buf;
	private int width;
	private int height;

	public GradientImage(int width, int height) {
		this.width = width;
		this.height = height;
		buf = BufferUtils.createByteBuffer(width * height * 4);
		for(int i=0;i<width*height;++i) {
			double dx = (i%width)/(double)width;
            double dy = (i/width)/(double)height;
            buf.put((byte)(dx * 255));
            buf.put((byte)(dy * 255));
            buf.put((byte)0);
            buf.put((byte)1.0);
		}
		buf.flip();
	}

	public ByteBuffer getRGBA() {
		return buf;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}
	
	

}
