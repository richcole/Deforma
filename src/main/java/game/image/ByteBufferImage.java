package game.image;

import java.nio.ByteBuffer;

import org.lwjgl.BufferUtils;

public class ByteBufferImage implements Image {

  private ByteBuffer buf;
  private int width;
  private int height;

  public ByteBufferImage(int width, int height) {
    this.width = width;
    this.height = height;
    this.buf = BufferUtils.createByteBuffer(width * height * 4);
  }
  
  public void write(byte r, byte g, byte b, byte a) {
      buf.put(r);
      buf.put(g);
      buf.put(b);
      buf.put(a);
  }
  
  public void finish() {
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
