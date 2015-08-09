package game.voxel;

public class Sphere {
	public Vector c;
	public double r;

	public Sphere(Vector v, double weight) {
		super();
		this.c = v;
		this.r = weight;
	}

	@Override
	public String toString() {
		return "Sphere [c=" + c + ", r=" + r + "]";
	}

}
