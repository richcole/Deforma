package game;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class SolidImage implements Image {

  private ByteBuffer buf;
  private int width;
  private int height;

  public SolidImage(int width, int height, double r, double g, double b) {
    this.width = width;
    this.height = height;
    buf = BufferUtils.createByteBuffer(width * height * 4);
    for (int i = 0; i < width * height; ++i) {
      buf.put((byte) (r * 255));
      buf.put((byte) (g * 255));
      buf.put((byte) (b * 255));
      buf.put((byte) 1.0);
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
