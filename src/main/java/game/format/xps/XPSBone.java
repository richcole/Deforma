package game.format.xps;

import game.basicgeom.Vector;

public class XPSBone {

	public String name;
	public int parentIndex;
	public Vector vector;

	public XPSBone(String name, int parentIndex, Vector vector) {
		this.name = name;
		this.parentIndex = parentIndex;
		this.vector = vector;
	}

}
