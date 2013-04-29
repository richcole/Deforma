package game;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.imageio.ImageIO;

public class Image {

  int width;
  int height;
  BufferedImage img;

  Image(File file) {
    try {
      img = ImageIO.read(file);
      width = img.getWidth();
      height = img.getHeight();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public Image(int width, int height) {
    this.width = width;
    this.height = height;
    this.img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
  }

  public ByteBuffer getByteBuffer() {
    ByteBuffer byteBuf = ByteBuffer.allocateDirect(width*height*3);
    byteBuf.order(ByteOrder.nativeOrder());
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        int rgb = img.getRGB(x, y);
        byteBuf.put((byte) ((rgb >> 16) & 0xff));
        byteBuf.put((byte) ((rgb >> 8) & 0xff));
        byteBuf.put((byte) ((rgb >> 0) & 0xff));
      }
    }
    byteBuf.flip();
    return byteBuf;
  }

  int getWidth() {
    return width;
  }

  int getHeight() {
    return height;
  }
  
  BufferedImage getImage() {
    return img;
  }

}
