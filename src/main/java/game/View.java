package game;


import org.lwjgl.opengl.DisplayMode;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.util.glu.GLU.*;

public class View implements HasInit {

  float perspAngle;
  float aspect;
  float zNear;
  float zFar;
  
  DisplayMode mode;
  
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
    perspAngle = 45f;
    aspect = 1.0f;
    zNear = 1.0f;
    zFar = 1500.0f;
  }
  
  public void initGL() {
    glEnable(GL_TEXTURE_2D);
    glShadeModel(GL_SMOOTH);       
    glEnable (GL_BLEND);
    glBlendFunc (GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    
    glEnable(GL_DEPTH_TEST);
    glDepthFunc(GL_LEQUAL);
    glDisable(GL_CULL_FACE);

    glClearColor(0.0f, 0.0f, 0.0f, 0.0f);               
    glClearDepth(1f);

    glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
    glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);
    glViewport(0, 0, (int)getWidth(), (int)getHeight());
  }

  public void perspectiveView() {
    glMatrixMode(GL_PROJECTION);
    glLoadIdentity();
    gluPerspective(getPerspAngle(), getAspect(), getZNear(), getZFar());
    glMatrixMode(GL_MODELVIEW);
    glLoadIdentity();
  }

  public void orthoView() {
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
