package game.models;

import game.Context;
import game.base.PhysicalObject;
import game.math.Vector;

public class LittleLight extends PhysicalObject {
  
  private static final Vector SIZE = new Vector(1, 1, 1, 1);
  
  Light light;
  Cube  cube;

  public LittleLight(Context context, Vector velocity, Vector position) {
    super(context, velocity, position, SIZE);
    light = new Light(context, position, context.getNextLightNumber());
    cube = new Cube(context, position, SIZE);
  }

  @Override
  public void move(Vector velocity) {
    super.move(velocity);
    light.move(velocity);
    cube.move(velocity);
  }
}
