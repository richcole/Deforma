package game.basicgeom;


public class Line {
	
	public Vector o;
	public Vector l;

	public Line(Vector o, Vector l) {
		this.o = o;
		this.l = l;
	}

	@Override
	public String toString() {
		return "Line [o=" + o + ", l=" + l + "]";
	}

}
