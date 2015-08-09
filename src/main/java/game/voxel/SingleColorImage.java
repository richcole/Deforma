package game.voxel;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class SingleColorImage implements Image {
	
	private ByteBuffer buf;
	private int width;
	private int height;
	
	public SingleColorImage(int width, int height, int r, int g, int b, int a) {
		this.width = width;
		this.height = height;
		buf = BufferUtils.createByteBuffer(width * height * 4);
		for(int i=0;i<width*height;++i) {
			if ( i == 0 ) {
				buf.put((byte)r);
				buf.put((byte)g);
				buf.put((byte)b);
				buf.put((byte)a);
			} else {
				buf.put((byte)0);
				buf.put((byte)0);
				buf.put((byte)0);
				buf.put((byte)255);
			}
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
