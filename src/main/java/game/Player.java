package game;

import org.lwjgl.util.glu.GLU;

public class Player implements SimObject {
  
  static int LEFT     = 0;
  static int UP       = 1;
  static int FORWARD  = 2;

  Vector p;
  Matrix b; // basic
  Matrix c; // transformed co-orindates

  double theta1, theta2;
  
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
   // left.glRotate(-theta2);
   // up.glRotate(-theta1);
   // p.minus().glTranslate();
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
    new PhysicalObject(context, forward().minus().scaleTo(20), p.minus(forward().scaleTo(10)), new Vector(1, 1, 1, 1));
  }
}
