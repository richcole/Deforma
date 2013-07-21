package game.utils;

import game.math.Matrix;
import game.math.Vector;

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
  
  public static void glNormal(Vector n) {
    GL11.glNormal3d(n.x(), n.y(), n.z());
  }

  public static void glVertex(Vector p) {
    GL11.glVertex3d(p.x(), p.y(), p.z());
  }
}
