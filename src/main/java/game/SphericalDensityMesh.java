package game;

import java.util.List;

import com.google.common.collect.Lists;

public class SphericalDensityMesh implements DensityFunction {
	
	int res;
	Vector bottomLeft;
	Vector topRight;
	
	double[] s;

	public SphericalDensityMesh(int res, Vector bottomLeft, Vector topRight) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
		this.res = res;
		s = new double[res*res*res];
		for(int i=0;i<res;++i) {
			for(int j=0;j<res;++j) {
				for(int k=0;k<res;++k) {
					s[index(i, j, k)] = 0;
				}
			}
		}
	}
	
	int index(int i, int j, int k) {
		return i*res*res + j*res + k;
	}
	
	// get index for a point in [0,res] space
	int index(Vector p) {
		int i = (int) Math.floor(p.x());
		int j = (int) Math.floor(p.y());
		int k = (int) Math.floor(p.z());
		return index(i, j, k);
	}

	// transform into [0, res] space
	Vector tr(Vector p) {
		return new Vector(
			res * ((p.x() - bottomLeft.x()) / (topRight.x() - bottomLeft.x())),
			res * ((p.y() - bottomLeft.y()) / (topRight.y() - bottomLeft.y())),
			res * ((p.z() - bottomLeft.z()) / (topRight.z() - bottomLeft.z())),
			1.0);
	}
		
	List<Sphere> neighbourhood(Vector v, double radius) {
		Vector c = tr(v);
		// dx, dy, dz are in [0, res] space
		double dx = res * radius / (topRight.x() - bottomLeft.x());
		double dy = res * radius / (topRight.y() - bottomLeft.y());
		double dz = res * radius / (topRight.z() - bottomLeft.z());
		double lx = Utils.clamp(c.x() - dx, 0, res-1);
		double ly = Utils.clamp(c.y() - dy, 0, res-1);
		double lz = Utils.clamp(c.z() - dz, 0, res-1);
		double ux = Utils.clamp(c.x() + dx, 0, res-1);
		double uy = Utils.clamp(c.y() + dy, 0, res-1);
		double uz = Utils.clamp(c.z() + dz, 0, res-1);
		
		List<Sphere> result = Lists.newArrayList();
		for(double x=lx;x<=ux;x+=1.0) {
			for(double y=ly;y<=uy;y+=1.0) {
				for(double z=lx;z<=uz;z+=1.0) {
					double fx = Math.floor(x);
					double fy = Math.floor(y);
					double fz = Math.floor(z);
					Vector p = new Vector(fx, fy, fz, 1.0);
					result.add(new Sphere(p, res - p.minus(c).length()));
				}
			}
		}
		
		return result;
	}
	
	List<Sphere> neighbourhood(Vector v) {
		Vector c = tr(v);
		// dx, dy, dz are in [0, res] space
		double dx = res / (topRight.x() - bottomLeft.x());
		double dy = res / (topRight.y() - bottomLeft.y());
		double dz = res / (topRight.z() - bottomLeft.z());
		double lx = Utils.clamp(c.x() - dx, 0, res-1);
		double ly = Utils.clamp(c.y() - dy, 0, res-1);
		double lz = Utils.clamp(c.z() - dz, 0, res-1);
		double ux = Utils.clamp(c.x() + dx, 0, res-1);
		double uy = Utils.clamp(c.y() + dy, 0, res-1);
		double uz = Utils.clamp(c.z() + dz, 0, res-1);
		
		List<Sphere> result = Lists.newArrayList();
		for(double x=lx;x<=ux;x+=1.0) {
			for(double y=ly;y<=uy;y+=1.0) {
				for(double z=lz;z<=uz;z+=1.0) {
					double fx = Math.floor(x);
					double fy = Math.floor(y);
					double fz = Math.floor(z);
					Vector p = new Vector(fx, fy, fz, 1.0);
					result.add(new Sphere(p, res - p.minus(c).length()));
				}
			}
		}
		
		return result;
	}

	public void add(Vector c, double r) {
		List<Sphere> n = neighbourhood(c, r);
		for(Sphere p: n) {
			s[index(p.c)] += p.r / 5;
		}
	}

	public double getDensity(Vector c) {
		List<Sphere> n = neighbourhood(c);
		double result = 0.0;
		for(Sphere p: n) {
			result += s[index(p.c)] * p.r;
		}
		return result;
	}

	public Vector getDensityDerivative(Vector v) {
		Vector r = Vector.Z;
		for(Sphere p: neighbourhood(v)) {
			r = p.c.minus(v).times(s[index(p.c)]);
		}
		return r.normalize();
	}

	public boolean isPositive(double d) {
		return d > 2.0;
	}

}
