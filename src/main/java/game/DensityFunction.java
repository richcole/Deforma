package game;

public interface DensityFunction {

	double getDensity(Vector p);
	Vector getDensityDerivative(Vector p);
	boolean isPositive(double d);
}
