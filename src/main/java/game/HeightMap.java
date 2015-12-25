package game;

import java.util.Random;

public class HeightMap {

	private int dx, dz;
	private double dy;
	private double height[];
	private Matrix tr;

	public HeightMap(int dx, int dy, int dz, Matrix tr) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		this.height = new double[dx * dz];
		this.tr = tr;
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

}
