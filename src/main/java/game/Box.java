package game;

public class Box {
	
	Vector bottomLeft;
	Vector topRight;
	
	public Box(Vector bottomLeft, Vector topRight) {
		this.bottomLeft = bottomLeft;
		this.topRight = topRight;
	}

	@Override
	public String toString() {
		return "Box [bottomLeft=" + bottomLeft + ", topRight=" + topRight + "]";
	}

	public Vector dp() {
		return topRight.minus(bottomLeft);
	}

	public Box orientTo(Vector forward) {
		if ( dp().dot(forward) > 0 ) {
			return new Box(topRight, bottomLeft);
		} else {
			return this;
		}
	}

	public Vector center() {
		return bottomLeft.plus(topRight).times(0.5);
	}

}