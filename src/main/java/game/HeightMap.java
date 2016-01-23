package game;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HeightMap {
	
	private static final Logger log = LoggerFactory.getLogger(HeightMap.class);

	private int dx, dz;
	private double dy;
	private double height[];
	private Matrix tr;
	private Matrix ntr;
	private Box box;

	public HeightMap(int dx, int dy, int dz, Matrix tr, Matrix ntr) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.height = new double[dx * dz];
		this.tr = tr;
		this.ntr = ntr;
		this.box = new Box(tr.times(Vector.U2.times(-dy)), tr.times(new Vector(dx, dy, dz, 1.0)));
		random();
		smooth(2);
	}

	public int index(int x, int z) {
		return z * dx + x;
	}

	public void random() {
		Random rand = new Random();
		for (int x = 0; x < dx; ++x) {
			for (int z = 0; z < dz; ++z) {
				height[index(x, z)] += dy * (rand.nextDouble() - 0.5);
			}
		}
	}

	void smooth(int n) {
		for(int i=0;i<n;++i) {
			for(int x=0;x<dx;++x) {
				for(int z=0;z<dz;++z) {
					int px = Math.max(0, x-1);
					int pz = Math.max(0, z-1);
					int nx = Math.min(dx-1, x+1);
					int nz = Math.min(dz-1, z+1);
					height[index(x,z)] = (
							height[index(x,z)] + 
							height[index(px,z)] + 
							height[index(nx,z)] + 
							height[index(x,pz)] + 
							height[index(px,pz)] + 
							height[index(nx,pz)] + 
							height[index(x,nz)] + 
							height[index(px,nz)] + 
							height[index(nx,nz)] ) / 9.0; 
				}
			}
		}
	}

	public int dx() {
		return dx;
	}

	public int dz() {
		return dz;
	}

	public double getHeight(int x, int z) {
		return height[index(x, z)];
	}

	public Vector transform(Vector vector) {
		return tr.times(vector);
	}

	public boolean contains(Vector p) {
		return box.contains(p);
	}

	public Vector getGravityMovement(Vector p, Vector left, double r, double speed) {
	  Vector p1 = ntr.times(p.plus(left));
	  Vector p2 = ntr.times(p.minus(left));
    Vector p3 = ntr.times(p);
		Vector tp1 = ntr.times(p1);
    Vector tp2 = ntr.times(p2);
    Vector tp3 = ntr.times(p3);
    double y1 = heightAt(tp1.x(), tp1.z());
    double y2 = heightAt(tp2.x(), tp2.z());
    double y3 = heightAt(tp3.x(), tp3.z());
    double ty1 = p1.y();
    double ty2 = p2.y();
    double ty3 = p3.y();
    double ty  = Math.min(Math.min(ty1, ty2), ty3);
    double y   = Math.max(Math.max(y1, y2), y3); 
		if ( ty < y ) {
			return tr.times(new Vector(0, y - ty, 0, 1.0));
		}
		else {
			return tr.times(new Vector(0, Math.min(speed, y - ty), 0, 1.0));
		}
	}

	private double heightAt(double x, double z) {
		int px = (int) Math.floor(x);
		int pz = (int) Math.floor(z);
		int nx = px + 1;
		int nz = pz + 1;
		double h1 = getHeight(px, pz);
		double h2 = getHeight(nx, pz);
		double h3 = getHeight(px, nz);
		double h4 = getHeight(nx, nz);
		
		double tx = (x - px);
		double tz = (z - pz);

		double m1 = Utils.mix(h1, h2, tx);		
		double m2 = Utils.mix(h3, h4, tx);		
		
		return Utils.mix(m1, m2, tz);
	}
}
