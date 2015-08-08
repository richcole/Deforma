package game.voxel;

import java.util.List;

import com.google.common.collect.Lists;

import game.math.Vector;

public class SphericalDensityField implements DensityFunction {
	
	List<PointPair> pairs;
	
	public SphericalDensityField() {
		pairs = Lists.newArrayList();
	}

	public double getDensity(Vector p) {
		double result = 0;
		for(PointPair pair: pairs) {
			double d = p.minus(pair.v).length();
			result += pair.weight / (pair.weight + d);
		}
		return result;
	}
	
	public Vector getDensityDerivative(Vector p) {
		Vector result = Vector.Z;
		for(PointPair pair: pairs) {
			Vector dp = p.minus(pair.v);
			double d = dp.length();
			result = result.plus(dp.times(pair.weight / (pair.weight + d)));
		}
		return result.normalize();
	}
	
	public boolean isPositive(double d) {
		return d > 0.5;
	}

	public void add(Vector p, double d) {
		pairs.add(new PointPair(p, d));
	}
	
}
