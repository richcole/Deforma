package game;

public class BBox {

	private Vector lower;
	private Vector upper;

	public BBox() {
	}
	
	public BBox(Vector lower, Vector upper) {
		this.lower = lower;
		this.upper = upper;
	}
	
	public void add(Vector v) {
		if ( lower == null ) {
			lower = new Vector(v);
		} else {
			lower = new Vector(Math.min(lower.x(), v.x()), Math.min(lower.y(), v.y()),  Math.min(lower.z(), v.z()), 1.0);  
		}
		if ( upper == null ) {
			upper = new Vector(v);
		} else {
			upper = new Vector(Math.max(upper.x(), v.x()), Math.max(upper.y(), v.y()),  Math.max(upper.z(), v.z()), 1.0);  
		}
	}

	@Override
	public String toString() {
		return "BBox [lower=" + lower + ", upper=" + upper + "]";
	}
	
}
