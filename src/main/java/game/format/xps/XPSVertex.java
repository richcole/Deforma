package game.format.xps;

import game.basicgeom.Vector;

public class XPSVertex {

	Vector v;
	Vector n;
	Vector c;
	float[] st;
	float[] tangents;
	float[] boneFloats;
	int[] boneIndexes;

	public XPSVertex(Vector v, Vector n, Vector c, float[] st, float[] tangents, int[] boneIndexes, float[] boneFloats) {
		this.v = v;
		this.n = n;
		this.c = c;
		this.st = st;
		this.tangents = tangents;
		this.boneIndexes = boneIndexes;
		this.boneFloats = boneFloats;
	}

}
