package game.voxel;

import game.math.Vector;

public class SphereDensity implements DensityFunction {
  
  Vector center;
  double radius;

  public SphereDensity(Vector center, double radius) {
    this.center = center;
    this.radius = radius;
  }


  @Override
  public double getDensity(Vector v) {
    return v.minus(center).length() - radius;
  }


  @Override
  public Vector getDensityDerivative(Vector v) {
    return v.times(2).minus(center.times(2));
  }
  
}