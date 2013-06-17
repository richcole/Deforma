package game.utils;

import game.math.Matrix;

import java.nio.DoubleBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;

public class GLUtils {

  public static void pushMatrix(Matrix m) {
    DoubleBuffer buf = BufferUtils.createDoubleBuffer(16);
    for(int j=0;j<4;++j) {
      for(int i=0;i<4;++i) {
        buf.put(m.get(i, j));
      }
    }
    buf.flip();
    GL11.glPushMatrix();
    GL11.glMultMatrix(buf);
  }
}
