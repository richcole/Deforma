package game;

import org.apache.log4j.Logger;

public class PhysicalObject implements SimObject {
  
  private static Logger logger = Logger.getLogger(PhysicalObject.class);
  
  Context context;
  Vector velocity;
  Rectangle rectangle;

  PhysicalObject(Context context, Vector velocity, Vector position, Vector size) {
    this.context = context;
    this.velocity = new Vector(velocity);
    this.rectangle = new Rectangle(context, position, size);
    context.getSimulator().register(this);
  }

  @Override
  public void tick() {
    velocity = velocity.plus(pos().minus().times(0.0001));
    for(SimObject o: context.getSimulator().getSimObjects()) {
      velocity = velocity.plus(collisionForce(o));
    }
    if ( velocity.length() > 1 ) {
      rectangle.move(velocity);
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

  @Override
  public Vector pos() {
    return rectangle.position;
  }
  
  @Override
  public double mass() {
    return rectangle.size.x()*rectangle.size.y();
  }

}
