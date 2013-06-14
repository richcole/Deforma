package game;

import game.math.Vector;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class SelectionRay {
  
  FloatBuffer projection;
  FloatBuffer modelView;
  IntBuffer viewport;
  FloatBuffer nearPos;
  FloatBuffer farPos;
  Context context;
  
  SelectionRay(Context context) {
    this.context = context;
    
    projection = BufferUtils.createFloatBuffer(16);
    modelView = BufferUtils.createFloatBuffer(16);
    viewport = BufferUtils.createIntBuffer(16);
    nearPos = BufferUtils.createFloatBuffer(3);
    farPos = BufferUtils.createFloatBuffer(3);
  }

  public void updateViewMatrix() {
    GL11.glGetInteger(GL11.GL_VIEWPORT, viewport);
    GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projection);
    GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelView);
  }
  
  public Vector getSelectionRay(float x, float y) {
    View view = context.getView();
    GLU.gluUnProject(x, y, view.zNear, modelView, projection, viewport, nearPos);
    GLU.gluUnProject(x, y, view.zFar, modelView, projection, viewport, farPos);
    return new Vector(farPos.get(0) - nearPos.get(0), farPos.get(1) - nearPos.get(1), farPos.get(2) - nearPos.get(2));    
  }
  
}
