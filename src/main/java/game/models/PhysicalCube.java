package game.models;

import game.Context;
import game.base.PhysicalObject;
import game.math.Vector;

public class PhysicalCube extends PhysicalObject {
  
  Cube cube;

  PhysicalCube(Context context, Vector velocity, Vector position, Vector size) {
    super(context, velocity, position, size);
    this.cube = new Cube(context, position, size);
  }

  @Override
  public void move(Vector velocity) {
    cube.move(velocity);
    super.move(velocity);
  }
  
}
