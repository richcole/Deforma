package game.image;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

public class BufferedImageImage implements Image {

  private BufferedImage img;

  public BufferedImageImage(BufferedImage img) {
    this.img = img;
  }

  public int getWidth() {
    return img.getWidth();
  }

  public int getHeight() {
    return img.getHeight();
  }

  public byte[] getRGBA() {
    int width = img.getWidth();
    int height = img.getHeight();
    WritableRaster rgbRaster = img.getRaster();
    WritableRaster alphaRaster = img.getAlphaRaster();
    byte[] buf = new byte[width * height * 4];
    for(int y=0;y<height;++y) {
      for(int x=0;x<width;++x) {
        int r = rgbRaster.getSample(x, height - y - 1, 0);
        int g = rgbRaster.getSample(x, height - y - 1, 1);
        int b = rgbRaster.getSample(x, height - y - 1, 2);
        int a;
        if ( alphaRaster != null ) {
          a = (byte) alphaRaster.getSample(x,  height - y - 1, 0);
        }
        else {
          a = (byte) 255;
        }
        int idx = (y*width+x)*4; 
        buf[idx+0] = (byte) r;
        buf[idx+1] = (byte) g;
        buf[idx+2] = (byte) b;
        buf[idx+3] = (byte) a;
      }
    }
    return buf;
  }

  public BufferedImage getImage() {
    return img;
  }

  public Graphics2D getGraphics() {
    return (Graphics2D)img.getGraphics();
  }
}
