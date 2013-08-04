package game.voxel;

import game.math.Vector;

public interface DensityFunction {

  double getDensity(Vector vector);
  Vector getDensityDerivative(Vector vector); 

}
