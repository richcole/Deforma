package game.model;

import java.util.List;

import com.google.common.collect.Lists;

public class AnimMesh {
	
	public static class AnimInfo {
		public String name;
		public int beginFrame;
		public int endFrame;
		public double timings[];
	}

	int nv; // number of vertices
	int nf; // number of faces
	int nb; // number of bones
	public double[] p; // 3 per vertex (position)
	public double[] n; // 3 per vertex (normals)
	public double[] t; // 2 per vertex per skin
	public int[] e; // 3 per face
	public int[] i; // image index 1 per vertex
	public List<String> imageList; // imageIndex

	public double[] b;       // one bone per vertex
	public double[] bTr;     // 16 per bone
	public AnimInfo[] anims; // one per anim
	public int numBones;

	public AnimMesh(int numVertices, int numFaces) {
		nv = numVertices;
		nf = numFaces;
		p = new double[3 * numVertices];
		n = new double[3 * numVertices];
		e = new int[3 * numFaces];
		t = new double[2 * numVertices];
		i = new int[1 * numVertices];
		imageList = Lists.newArrayList();
	}
}
