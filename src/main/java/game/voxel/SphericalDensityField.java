package game.voxel;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class SphericalDensityField implements DensityFunction {
	
	private static final Logger log = LoggerFactory.getLogger(SphericalDensityField.class);
	
	List<Sphere> pairs;
	
	public SphericalDensityField() {
		pairs = Lists.newArrayList();
	}

	public double getDensity(Vector p) {
		double result = 0;
		for(Sphere pair: pairs) {
			double d = p.minus(pair.c).length();
			result += pair.r / (pair.r + d);
		}
		return result;
	}
	
	public Vector getDensityDerivative(Vector p) {
		Vector result = Vector.Z;
		for(Sphere pair: pairs) {
			Vector dp = p.minus(pair.c);
			double d = dp.length();
			result = result.plus(dp.times(pair.r / (pair.r + d)));
		}
		return result.normalize();
	}
	
	public boolean isPositive(double d) {
		return d > 0.5;
	}

	public void add(Vector p, double d) {
		pairs.add(new Sphere(p, d));
	}
	
	public Vector intersection(Vector bottomLeft, Vector topRight, Vector dp) {
		Vector p = bottomLeft;
		double pd = getDensity(p);
		double nd;
		double f = topRight.minus(bottomLeft).dot(dp);
		Vector np;
		boolean b = isPositive(pd);
		boolean n;
		while(true) {
			np = p.plus(dp);
			nd = getDensity(np);
			n = isPositive(nd);
			log.info("np " + np + " nd " + nd + " dp " + dp);
			if ( n != b ) {
				return p; // should mix p and np
			}
			if ( np.minus(bottomLeft).dot(dp) > f ) {
				log.info("f " + f + " " + np.minus(bottomLeft).dot(dp) + " nd " + nd);
				return null;
			}
			p = np;
			b = n;
			pd = nd;
		}
	}
	
	
}
