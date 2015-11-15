package game;

public class Line {
	
	Vector o;
	Vector l;

	Line(Vector o, Vector l) {
		this.o = o;
		this.l = l;
	}

	@Override
	public String toString() {
		return "Line [o=" + o + ", l=" + l + "]";
	}

}
