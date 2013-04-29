package game;

import javax.vecmath.Vector3f;

public class BorgShipRectangle extends Rectangle {

  public BorgShipRectangle(Context context) {
    super(context, new Vector3f(100, -50, -100), new Vector3f(200, 200, 200));
  }

}
