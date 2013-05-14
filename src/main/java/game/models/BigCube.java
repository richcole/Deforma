package game.models;

import game.Context;
import game.math.Vector;

public class BigCube extends PhysicalCube {

  private static final Vector SIZE     = new Vector(10, 10, 10, 1);
  private static final Vector POSITION = new Vector(100, 100, 100, 1);
  private static final Vector VELOCITY = new Vector(0, 0, 0, 1);

  Cube rectangle;
  
  public BigCube(Context context) {
    super(context, VELOCITY, POSITION, SIZE);
  }
  
  public void move(Vector velocity) {
    
  }

}
