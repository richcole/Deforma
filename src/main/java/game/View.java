package game;


import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_LEQUAL;
import static org.lwjgl.opengl.GL11.GL_LIGHTING;
import static org.lwjgl.opengl.GL11.GL_LIGHT_MODEL_AMBIENT;
import static org.lwjgl.opengl.GL11.GL_MODELVIEW;
import static org.lwjgl.opengl.GL11.GL_NICEST;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_PERSPECTIVE_CORRECTION_HINT;
import static org.lwjgl.opengl.GL11.GL_PROJECTION;
import static org.lwjgl.opengl.GL11.GL_SMOOTH;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNPACK_ALIGNMENT;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glClearDepth;
import static org.lwjgl.opengl.GL11.glDepthFunc;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glHint;
import static org.lwjgl.opengl.GL11.glLightModel;
import static org.lwjgl.opengl.GL11.glLoadIdentity;
import static org.lwjgl.opengl.GL11.glMatrixMode;
import static org.lwjgl.opengl.GL11.glOrtho;
import static org.lwjgl.opengl.GL11.glPixelStorei;
import static org.lwjgl.opengl.GL11.glShadeModel;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.util.glu.GLU.gluPerspective;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class View {

  float perspAngle;
  float aspect;
  float zNear;
  float zFar;
  
  DisplayMode mode;
  Context context;
  
  View(Context context) {
    this.context = context;
    perspAngle = 45f;
    aspect = 1.0f;
    zNear = 1.0f;
    zFar = 100000.0f;
    mode = Display.getDisplayMode();
  }
  
  public float getPerspAngle() {
    return perspAngle;
  }
  
  public void setPerspAngle(float perspAngle) {
    this.perspAngle = perspAngle;
  }
  
  public float getAspect() {
    return aspect;
  }
  
  public void setAspect(float aspect) {
    this.aspect = aspect;
  }
  
  public float getZNear() {
    return zNear;
  }
  
  public void setZNear(float zNear) {
    this.zNear = zNear;
  }
  
  public float getZFar() {
    return zFar;
  }
  
  public void setZFar(float zFar) {
    this.zFar = zFar;
  }

  public DisplayMode getMode() {
    return mode;
  }

  public void setMode(DisplayMode mode) {
    this.mode = mode;
  }
  
  public float getWidth() {
    return mode.getWidth();
  }

  public float getHeight() {
    return mode.getHeight();
  }

  public void init() {
    mode = Display.getDisplayMode();

    glEnable(GL_TEXTURE_2D);
    glShadeModel(GL_SMOOTH);       
    glEnable (GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glDisable(GL_CULL_FACE);
    
    glEnable(GL_LIGHTING);
    
    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               
    glClearDepth(1f);

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    glViewport(0, 0, (int)getWidth(), (int)getHeight());
    
    glLightModel(GL_LIGHT_MODEL_AMBIENT, context.getColors().getGray9());
  }

  public void perspectiveView() {
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(getPerspAngle(), getAspect(), getZNear(), getZFar());
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
  }
  
  public void orthoView() {
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    glOrtho(0, getWidth(), getHeight(), 0, 1, -1);
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
  }

  public void clear() {
    glMatrixMode(GL_MODELVIEW);
    glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
  }

}
