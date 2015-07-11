package game;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.google.common.io.Resources;

public class ImageResource implements Image {
	
	private BufferedImage img;

	public ImageResource(String path) {
		try {
			img = ImageIO.read(Resources.getResource(path));
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load " + path, e);
		}
	}

	public int getWidth() {
		return img.getWidth();
	}

	public int getHeight() {
		return img.getHeight();
	}

	public ByteBuffer getRGBA() {
		int[] data = img.getRGB(0, 0, img.getWidth(), img.getHeight(), null, 0, img.getWidth());
		ByteBuffer buf = BufferUtils.createByteBuffer(data.length * 4);
		for(int i=0;i<data.length;++i) {
			int red   = (data[i] >> 16) & 0xff;
			int green = (data[i] >> 8) & 0xff;
			int blue  = (data[i] >> 0) & 0xff;
			int alpha = (data[i] >> 24) & 0xff;
			buf.put((byte)red);
			buf.put((byte)green);
			buf.put((byte)blue);
			buf.put((byte)alpha);
		}
		buf.flip();
		return buf;
	}
	
	

}
