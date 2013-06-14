package game;

import game.base.SimObject;
import game.math.Matrix;
import game.math.Vector;
import game.models.Creature;
import game.models.LittleCube;
import game.models.LittleLight;

import org.lwjgl.util.glu.GLU;

public class Player implements SimObject {
  
  static Vector LEFT     = Vector.LEFT;
  static Vector UP       = Vector.UP;
  static Vector NORMAL   = Vector.NORMAL;

  Vector pos;
  Vector left;
  Vector up;
  Vector normal;

  double theta1, theta2;
  
  boolean movingDownward = false;
  boolean movingUpward = false;
  boolean movingForward = false;
  boolean movingBackward = false;
  boolean movingLeft = false;
  boolean movingRight = false;

  private Context context;
  private Creature selectedCreature;
  
  public Player(Context context) {
    this.context = context;
    this.pos = Vector.ZERO;
    this.theta1 = 0;
    this.theta2 = 0;
  }

  Vector getLeft() {
    return left;
  }
  
  Vector getUp() {
    return up;
  }

  Vector getNormal() {
    return normal;
  }

  public synchronized void render() {
    theta1 = context.getInputDevice().getX() * 6.283f / 5000.0f;
    theta2 = context.getInputDevice().getY() * 6.283f / 5000.0f;

    Matrix rotUp = Matrix.rot(-theta1, UP);
    left = rotUp.times(LEFT);
    normal = rotUp.times(NORMAL);
    
    Matrix rotLeft = Matrix.rot(-theta2, left);
    normal = rotLeft.times(normal);
    up = rotLeft.times(UP);
    
    Vector a = pos.plus(getNormal());
    Vector u = getUp();
    GLU.gluLookAt((float)pos.x(), (float)pos.y(), (float)pos.z(), (float)a.x(), (float)a.y(), (float)a.z(), (float)u.x(), (float)u.y(), (float)u.z());
    context.getSelectionRay().updateViewMatrix();
  }

  @Override
  public void tick() {
    float velocity = 2;
    if ( movingForward ) {
      pos = pos.plus(getNormal().scaleTo(velocity));
    }
    if ( movingBackward ) {
      pos = pos.plus(getNormal().scaleTo(-velocity));
    }
    if ( movingLeft ) {
      pos = pos.plus(getLeft().scaleTo(velocity));
    }
    if ( movingRight ) {
      pos = pos.plus(getLeft().scaleTo(-velocity));
    }
    if ( movingUpward ) {
      pos = pos.plus(getUp().scaleTo(velocity));
    }
    if ( movingDownward ) {
      pos = pos.plus(getUp().scaleTo(-velocity));
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
    Creature creature = context.newCreature();
    creature.register();
  }

  public void fireLight() {
    LittleLight littleLight = new LittleLight(context, getNormal().scaleTo(20), pos.plus(getNormal().scaleTo(10)));
    littleLight.register();
  }

  @Override
  public Vector getPos() {
    return pos;
  }

  @Override
  public double getMass() {
    return 100;
  }

  public void register() {
    context.getSimulator().register(this);
  }

  public void setSelectedCreature(Creature selectedCreature) {
    this.selectedCreature = selectedCreature;
  }

  public Creature getSelectedCreature() {
    return selectedCreature;
  }
}
