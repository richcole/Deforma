package game.voxel;

import game.basicgeom.Vector;

public interface DensityFunction {

	double getDensity(Vector p);
	Vector getDensityDerivative(Vector p);
	boolean isPositive(double d);
	
}
