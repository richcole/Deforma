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
    if ( velocity.length() > 1 ) {
      rectangle.move(velocity);
      velocity = velocity.times(0.95f);
    }
  }
  
}
