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
    double d = radius - v.minus(center).length();
    return d;
  }


  @Override
  public Vector getDensityDerivative(Vector v) {
    double d = radius - v.minus(center).length();
    return center.minus(v).normalize();
  }
  
}