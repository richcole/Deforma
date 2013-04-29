package game;

import javax.vecmath.Vector3f;

public class PhysicalObject implements SimObject {
  
  Context context;
  Vector3f velocity;
  Rectangle rectangle;

  PhysicalObject(Context context, Vector3f velocity, Vector3f position, Vector3f size) {
    this.context = context;
    this.velocity = new Vector3f(velocity);
    this.rectangle = new Rectangle(context, position, size);
    context.getSimulator().register(this);
  }

  @Override
  public void tick() {
    rectangle.getPosition().add(velocity);
    velocity.scale(0.95f);
  }
  
}
