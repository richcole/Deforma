package game;

import java.util.Random;

public class Terrain {

	int dx;
	int dz;
	double h;
	double heights[];
	private Box box;
	private double res;
	
	public Terrain(Box box, double res) {
		this.box = box;
		this.res = res;
		h = box.dp().z();
		dx = (int) Math.floor(box.dp().x() / res) + 1;
		dz = (int) Math.floor(box.dp().y() / res) + 1;
		heights = new double[dx*dz];
		for(int i=0;i<dx*dz;++i) {
			heights[i] = new Random().nextDouble() * h;
		}
	}
	
	public int index(Vector p) {
		Vector v = p.minus(box.bottomLeft).times(res);
		int i = (int) Math.floor(v.x());
		int j = (int) Math.floor(v.z());
		return (j*dz+i);
	}
	
	
	
}
