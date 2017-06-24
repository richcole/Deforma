package game.model;

import com.google.common.collect.Lists;

import component.Dimension;
import game.math.Vector;

public class MeshFactory {

	public Mesh newCubeMesh(String imageName) {
		MeshBuilder mb = new MeshBuilder();

		for(Dimension dim: Dimension.ALL_DIMS) {
			Vector n = dim.getFwd();
			Vector blt = Vector.BL;
			Vector tlt = Vector.TL;
			Vector trt = Vector.TR;
			Vector brt = Vector.BR;
			Vector bl = dim.getFwd().minus(dim.getRight()).minus(dim.getUp());
			Vector tl = dim.getFwd().minus(dim.getRight()).plus(dim.getUp());
			Vector tr = dim.getFwd().plus(dim.getRight()).plus(dim.getUp());
			Vector br = dim.getFwd().plus(dim.getRight()).minus(dim.getUp());
			mb.addTri(imageName, tr, tl, bl, n, trt, tlt, blt);
			mb.addTri(imageName, br, tr, bl, n, brt, trt, blt);
		}

		return mb.build();
	}

	public Mesh newSquareMesh(Vector center, Vector right, Vector up, String imageName) {

		Mesh mesh = new Mesh(4 * 3, 2);

		Vector n = right.cross(up).normalize();
		Vector bl = center.minus(right).minus(up);
		Vector tl = center.minus(right).plus(up);
		Vector br = center.plus(right).minus(up);
		Vector tr = center.plus(right).plus(up);

		setVertex(mesh, 0, bl, n, 0, 0);
		setVertex(mesh, 1, tl, n, 0, 1);
		setVertex(mesh, 2, tr, n, 1, 1);
		setVertex(mesh, 3, br, n, 1, 0);

		mesh.e = new int[6];
		mesh.e[0] = 0;
		mesh.e[1] = 1;
		mesh.e[2] = 2;

		mesh.e[3] = 0;
		mesh.e[4] = 2;
		mesh.e[5] = 3;

		mesh.i = new int[mesh.nv];
		for (int i = 0; i < mesh.nv; ++i) {
			mesh.i[i] = 0;
		}

		mesh.imageList = Lists.newArrayList(imageName);

		return mesh;
	}

	public Mesh newSquareMeshOld(Vector center, Vector left, Vector up, String imageName) {

		Mesh mesh = new Mesh(4 * 3, 2);

		Vector n = left.cross(up).normalize();
		Vector bl = center.plus(left).minus(up);
		Vector tl = center.plus(left).plus(up);
		Vector br = center.minus(left).minus(up);
		Vector tr = center.minus(left).plus(up);

		setVertex(mesh, 0, bl, n, 0, 0);
		setVertex(mesh, 1, tl, n, 0, 1);
		setVertex(mesh, 2, tr, n, 1, 1);
		setVertex(mesh, 3, br, n, 1, 0);

		mesh.e = new int[6];
		mesh.e[0] = 0;
		mesh.e[1] = 1;
		mesh.e[2] = 2;

		mesh.e[3] = 0;
		mesh.e[4] = 2;
		mesh.e[5] = 3;

		mesh.i = new int[mesh.nv];
		for (int i = 0; i < mesh.nv; ++i) {
			mesh.i[i] = 0;
		}

		mesh.imageList = Lists.newArrayList(imageName);

		return mesh;
	}

	private void setVertex(Mesh mesh, int index, Vector p, Vector n, double tx, double ty) {
		mesh.p1[index * 3 + 0] = p.x();
		mesh.p1[index * 3 + 1] = p.y();
		mesh.p1[index * 3 + 2] = p.z();

		mesh.n[index * 3 + 0] = n.x();
		mesh.n[index * 3 + 1] = n.y();
		mesh.n[index * 3 + 2] = n.z();

		mesh.t[index * 2 + 0] = tx;
		mesh.t[index * 2 + 1] = ty;
	}

}
