package game.voxel;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;

public class ImageResource implements Image {
	
	private BufferedImage img;

	public ImageResource(String path) {
		try {
			URL url = Resources.getResource(path);
			Preconditions.checkNotNull(url);
			img = ImageIO.read(url);
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load " + path, e);
		}
	}

	public ImageResource(File file) {
		try {
			Preconditions.checkNotNull(file);
			img = ImageIO.read(file);
			if ( img == null ) {
				throw new RuntimeException("Couldn't load " + file);
			}
		} catch(IOException e) {
			throw new RuntimeException("Couldn't load " + file, e);
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