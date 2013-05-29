package game;

import game.base.SimObject;
import game.math.Matrix;
import game.math.Vector;
import game.models.LittleCube;
import game.models.LittleLight;

import org.lwjgl.util.glu.GLU;

public class Player implements SimObject {
  
  static int LEFT     = 0;
  static int UP       = 1;
  static int FORWARD  = 2;

  Vector p;
  Matrix b; // basic
  Matrix c; // transformed co-orindates

  double theta1, theta2;
  
  boolean movingDownward = false;
  boolean movingUpward = false;
  boolean movingForward = false;
  boolean movingBackward = false;
  boolean movingLeft = false;
  boolean movingRight = false;

  private Context context;
  
  public Player(Context context) {
    this.context = context;
    this.p = Vector.ZERO;
    this.theta1 = 0;
    this.theta2 = 0;
    
    this.b = Matrix.rows(Vector.U1, Vector.U2, Vector.U3, Vector.ZERO);
    this.c = b;

    context.getSimulator().register(this);
  }

  Vector left() {
    return c.row(LEFT);
  }
  
  Vector up() {
    return c.row(UP);
  }

  Vector forward() {
    return c.row(FORWARD);
  }

  public synchronized void render() {
    theta1 = context.getInputDevice().getX() * 6.283f / 5000.0f;
    theta2 = context.getInputDevice().getY() * 6.283f / 5000.0f;

    c = b.times(Matrix.rot(theta1, b.row(UP)));
    Vector left = c.row(LEFT);
    c = c.times(Matrix.rot(theta2, left));

    Vector a = p.plus(forward());
    Vector up = up();
    GLU.gluLookAt((float)p.x(), (float)p.y(), (float)p.z(), (float)a.x(), (float)a.y(), (float)a.z(), (float)up.x(), (float)up.y(), (float)up.z());
  }

  @Override
  public void tick() {
    float velocity = 2;
    if ( movingForward ) {
      p = p.plus(forward().scaleTo(velocity));
    }
    if ( movingBackward ) {
      p = p.plus(forward().scaleTo(-velocity));
    }
    if ( movingLeft ) {
      p = p.plus(left().scaleTo(velocity));
    }
    if ( movingRight ) {
      p = p.plus(left().scaleTo(-velocity));
    }
    if ( movingUpward ) {
      p = p.plus(up().scaleTo(velocity));
    }
    if ( movingDownward ) {
      p = p.plus(up().scaleTo(-velocity));
    }
  }

  public void setMovingDownward(boolean movingForward) {
    this.movingDownward = movingForward;
  }

  public void setMovingUpward(boolean movingForward) {
    this.movingUpward = movingForward;
  }

  public void setMovingForward(boolean movingForward) {
    this.movingForward = movingForward;
  }

  public void setMovingBackward(boolean movingBackward) {
    this.movingBackward = movingBackward;
  }

  public void setMovingLeft(boolean movingLeft) {
    this.movingLeft = movingLeft;
  }

  public void setMovingRight(boolean movingRight) {
    this.movingRight = movingRight;
  }

  public void fire() {
    LittleCube littleCube = new LittleCube(context, forward().scaleTo(20), p.plus(forward().scaleTo(10)));
    littleCube.register();
  }

  public void fireLight() {
    LittleLight littleLight = new LittleLight(context, forward().scaleTo(20), p.plus(forward().scaleTo(10)));
    littleLight.register();
  }

  @Override
  public Vector pos() {
    return p;
  }

  @Override
  public double mass() {
    return 100;
  }
  
  
}
