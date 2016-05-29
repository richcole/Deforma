package game.model;

import java.util.List;

import com.google.common.collect.Lists;

public class Mesh implements Cloneable {
	int nv; // number of vertices
	int nf; // number of faces
	int nb; // number of bones
	public double[] p1; // 3 per vertex (position)
	public double[] p2; // 3 per vertex (position)
	public double[] n; // 3 per vertex (normals)
	public double[] t; // 2 per vertex per skin
	public int[] e; // 3 per face
	public int[] i; // image index 1 per vertex
	public List<String> imageList; // imageIndex

	public Mesh(int numVertices, int numFaces) {
		nv = numVertices;
		nf = numFaces;
		p1 = new double[3 * numVertices];
		p2 = new double[3 * numVertices];
		n = new double[3 * numVertices];
		e = new int[3 * numFaces];
		t = new double[2 * numVertices];
		i = new int[1 * numVertices];
		imageList = Lists.newArrayList();
	}

	public Mesh clone() {
		try {
			return (Mesh) super.clone();
		} catch (Exception e) {
			throw new RuntimeException("Unable to clone", e);
		}
	}
}