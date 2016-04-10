package game.voxel;

import game.basicgeom.Vector;

public class SphericalDensityFunction implements DensityFunction {
	
	Vector c;
	double r;

	public SphericalDensityFunction(Vector c, double r) {
		this.c = c;
		this.r = r;
	}

	public double getDensity(Vector p) {
		double d = p.minus(c).length();
		return r / (r + d);
	}

	public Vector getDensityDerivative(Vector p) {
		return p.minus(c);
	}

	public boolean isPositive(double d) {
		return d > 0.5;
	}

}
