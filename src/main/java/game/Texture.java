package game;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;

public class Texture {

  int width;
  int height;
  int textureId;
  
  Texture(File file) {
    this(new Image(file));
  }
  
  Texture(Image img) {
    width = img.getWidth();
    height = img.getHeight();
    allocateTexture(width, height, getByteBuf(img.getBytes()));
  }

  private ByteBuffer getByteBuf(byte[] buf) {
    ByteBuffer byteBuf = ByteBuffer.allocateDirect(buf.length);
    byteBuf.order(ByteOrder.nativeOrder());
    byteBuf.put(buf, 0, buf.length);
    byteBuf.flip();
    return byteBuf;
  }
  
  Texture(int width, int height) {
    int len = width*height*3;
    byte[] buf = new byte[len];
    for(int i=0;i<width;++i) {
      for(int j=0;j<height;++j) {
        buf[(j*width*3)+i*3] = (byte)(j % 256);
      }
    }
    allocateTexture(width, height, getByteBuf(buf));
  }

  private void allocateTexture(int width, int height, ByteBuffer byteBuf) {
    allocateTextureId();
    glBindTexture(GL_TEXTURE_2D, textureId);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
    glTexEnvf(GL_TEXTURE_ENV, GL_TEXTURE_ENV_MODE, GL_MODULATE);
    glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height, 0, GL_RGB, GL_UNSIGNED_BYTE, byteBuf);
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
}
