package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class MutableTexture {

  int textureId;
  Image img;
  
  MutableTexture(int width, int height) {
    img = new Image(width, height);
    allocateTextureId();
    glBindTexture(GL_TEXTURE_2D, textureId);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
  }
  
  private ByteBuffer getByteBuffer(int[] buf) {
    ByteBuffer byteBuf = ByteBuffer.allocateDirect(buf.length*4);
    byteBuf.order(ByteOrder.nativeOrder());
    byteBuf.rewind();
    for(int i=0;i<buf.length;++i) {
      byteBuf.put((byte)(buf[i] >> 8));
      byteBuf.put((byte)(buf[i] >> 16));
      byteBuf.put((byte)(buf[i] >> 24));
      byteBuf.put((byte)(buf[i] >> 0));
    }
    byteBuf.flip();
    return byteBuf;
  }

  private void updateTexture(int width, int height, ByteBuffer byteBuf) {
    glBindTexture(GL_TEXTURE_2D, textureId);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, byteBuf);
  }

  private void allocateTextureId() {
    IntBuffer textureIdBuf = BufferUtils.createIntBuffer(1);
    glGenTextures(textureIdBuf);
    textureId = textureIdBuf.get(0);
    if ( textureId == 0 ) {
      throw new RuntimeException("Unable to allocate texture.");
    }
  }
  
  public void bind() {
    glBindTexture(GL_TEXTURE_2D, textureId);
  }
  
  public void update() {
    DataBufferInt dataBuffer = (DataBufferInt) img.getImage().getAlphaRaster().getDataBuffer();
    updateTexture(img.getWidth(), img.getHeight(), getByteBuffer(dataBuffer.getData()));
  }
  
  public Graphics2D getGraphics() {
    return img.getImage().createGraphics();
  }

  public float getWidth() {
    return img.getWidth();
  }

  public float getHeight() {
    return img.getHeight();
  }

  public Image getImage() {
    return img;
  }

}
