package game;

import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static org.lwjgl.util.glu.GLU.*;

public class LookAt implements HasInit, HasRender  {
  private static Logger logger = Logger.getLogger(LookAt.class);
  
  @Inject InputDevice inputDevice;
  
  Vector3d e, c, u;
  double ru, rs;
  
  double cru, sru;
  double crs, srs;
  Vector3d f;
  Vector3d s;
  Vector3d nf;
  Vector3d nu;
  Vector3d ns;
  Vector3d nc;
  
  boolean movingForward = false;
  boolean movingBackward = false;
  boolean movingLeft = false;
  boolean movingRight = false;
  
  synchronized void tick() {
    float velocity = 2;
    if ( movingForward ) {
      Vector3d tr = new Vector3d(forward());
      tr.normalize();
      tr.scale(velocity);
      move(tr);
    }
    if ( movingBackward ) {
      Vector3d tr = new Vector3d(forward());
      tr.normalize();
      tr.scale(velocity);
      tr.negate();
      move(tr);
    }
    if ( movingLeft ) {
      Vector3d tr = new Vector3d(sideways());
      tr.normalize();
      tr.scale(velocity);
      move(tr);
    }
    if ( movingRight ) {
      Vector3d tr = new Vector3d(sideways());
      tr.normalize();
      tr.scale(velocity);
      tr.negate();
      move(tr);
    }
  }
  
  private void addScale(Vector3d x, double s, Vector3d y) {
    x.x += y.x *s;
    x.y += y.y *s;
    x.z += y.z *s;
  }

  synchronized void recalc() {
    cru = (float) cos(ru);
    sru = (float) sin(ru);
    crs = (float) cos(-rs);
    srs = (float) sin(-rs);

    f   = new Vector3d(c);
    f.sub(e);
    f.normalize();

    Vector3d x = new Vector3d();
    x   = new Vector3d(c);
    x.sub(e);

    s = new Vector3d();
    s.cross(x, u);
    s.normalize();
    s.negate();
    
    nf  = new Vector3d(f);
    nf.scale(cru * crs);
    addScale(nf, sru, u);
    addScale(nf, cru * srs, s);
    
    nu  = new Vector3d(f);
    nu.scale(-sru * crs);
    addScale(nu, cru, u);
    addScale(nu, -sru * srs, s);

    ns  = new Vector3d(f);
    ns.scale(-cru * srs);
    addScale(ns, sru, u);
    addScale(ns, cru * crs, s);
    
    nc = new Vector3d(e);
    nc.add(nf);
  }
  
  void rotate(float rs, float ru) {
    this.rs = rs;
    this.ru = ru;
  }

  public synchronized void render() {
    rs = inputDevice.getX() * 6.283f / 5000.0f;
    ru = inputDevice.getY() * 6.283f / 5000.0f;
    
    recalc();
    gluLookAt(
      (float) e.x,  (float) e.y,  (float) e.z, 
      (float) nc.x, (float) nc.y, (float) nc.z, 
      (float) nu.x, (float) nu.y, (float) nu.z
    );
  }

  Vector3d forward() {
    return nf;
  }

  Vector3d sideways() {
    return ns;
  }

  private synchronized void move(Vector3d tr) {
    c.add(tr);
    e.add(tr);
  }

  synchronized public void init() {
    e = new Vector3d(0.0f,  0.0f, -20.0f); 
    c = new Vector3d(0.0f,  0.0f,   1.0f); 
    u = new Vector3d(0.0f,  1.0f,   0.0f);
    ru = 0;
    rs = 0;
    recalc();
  }

  public void moveForward(boolean pressed) {
    movingForward = pressed;
  }

  public void moveBackward(boolean pressed) {
    movingBackward = pressed;
  }

  public void moveLeft(boolean pressed) {
    movingLeft = pressed;
  }

  public void moveRight(boolean pressed) {
    movingRight = pressed;
  }
}
