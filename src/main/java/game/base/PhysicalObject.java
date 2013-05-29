package game.base;

import game.Context;
import game.math.Vector;

import org.apache.log4j.Logger;

public class PhysicalObject implements SimObject {
  
  private static Logger logger = Logger.getLogger(PhysicalObject.class);
  
  Context context;
  Vector velocity;
  Vector position;
  Vector size;
  
  protected PhysicalObject(Context context, Vector velocity, Vector position, Vector size) {
    this.context = context;
    this.velocity = new Vector(velocity);
    this.position = position;
    this.size = size;
  }

  @Override
  public void tick() {
    velocity = velocity.plus(pos().minus().times(0.0001));
    for(SimObject o: context.getSimulator().getSimObjects()) {
      velocity = velocity.plus(collisionForce(o));
    }
    if ( velocity.length() > 1 ) {
      move(velocity);
      velocity = velocity.times(0.95f);
    }
  }
  
  public Vector collisionForce(SimObject o) {
    if ( o != this ) {
      double r  = o.mass();
      Vector v  = pos().minus(o.pos());
      double vls = v.lengthSquared(); 
      if ( vls < 1 ) {
        vls = 1;
      }
      return v.scaleTo(r/vls);
    }
    else {
      return Vector.ZERO;
    }
  }
  
  public void move(Vector velocity) {
    position = position.plus(velocity);
  }

  @Override
  public Vector pos() {
    return position;
  }
  
  @Override
  public double mass() {
    return size.x()*size.y();
  }
  
  public void register() {
    context.getSimulator().register(this);    
  }

}
