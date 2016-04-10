package game.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class BufferedImageImage implements Image {

  private BufferedImage img;

  public BufferedImageImage(int width, int height) {
    this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  public int getWidth() {
    return img.getWidth();
  }

  public int getHeight() {
    return img.getHeight();
  }

  public ByteBuffer getRGBA() {
    int width = img.getWidth();
    int height = img.getHeight();
    WritableRaster rgbRaster = img.getRaster();
    WritableRaster alphaRaster = img.getAlphaRaster();
    ByteBuffer buf = BufferUtils.createByteBuffer(width * height * 4);
    for(int y=1;y<=height;++y) {
      for(int x=0;x<width;++x) {
        int r = rgbRaster.getSample(x, height - y, 0);
        int g = rgbRaster.getSample(x, height - y, 1);
        int b = rgbRaster.getSample(x, height - y, 2);
        int a = alphaRaster.getSample(x,  height - y, 0);
        buf.put((byte)r);
        buf.put((byte)g);
        buf.put((byte)b);
        buf.put((byte)a);
      }
    }
    buf.flip();
    return buf;
  }

  public BufferedImage getImage() {
    return img;
  }
  
}
