package game;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Image {

  int width;
  int height;
  BufferedImage img;
  byte buf[];

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

  public byte[] getBytes() {
    buf = new byte[width * height * 3];
    for (int y = 0; y < height; ++y) {
      for (int x = 0; x < width; ++x) {
        int rgb = img.getRGB(x, y);
        buf[(((y * width) + x) * 3) + 2] = (byte) ((rgb >> 0) & 0xff);
        buf[(((y * width) + x) * 3) + 1] = (byte) ((rgb >> 8) & 0xff);
        buf[(((y * width) + x) * 3) + 0] = (byte) ((rgb >> 16) & 0xff);
      }
    }
    return buf;
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
