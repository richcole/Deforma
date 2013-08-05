package game.voxel;

import game.math.Vector;

public class ScaleTransform implements Transform {

  private double scale;

  public ScaleTransform(double scale) {
    this.scale = scale;
  }

  @Override
  public Vector transform(Vector vector) {
    return vector.times(scale);
  }

  @Override
  public Vector transformNormal(Vector q1, Vector n1) {
    return n1;
  }
  
}
