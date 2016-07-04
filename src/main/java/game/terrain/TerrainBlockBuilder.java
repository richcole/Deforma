package game.terrain;

import game.math.Matrix;
import game.math.Vector;
import game.model.Mesh;
import game.model.MeshBuilder;

public class TerrainBlockBuilder {
	
	private Matrix tr;
	private TerrainBlockTypes blockTypes;

	public TerrainBlockBuilder(TerrainBlockTypes blockTypes, Matrix tr) {
		this.blockTypes = blockTypes;
		this.tr = tr;
	}
	
	public Mesh createMesh(Terrain t, Vector p, int d) {
		MeshBuilder mb = new MeshBuilder();
		mb.setTr(tr);
		for(int x=0;x<d;++x) {
			for(int y=0;y<d;++y) {
				for(int z=0;z<d;++z) {
					Vector v = p.plus(new Vector(x, y, z, 1.0));
					addFace(t, mb, v, Vector.U1, Vector.U2, Vector.U3);
					addFace(t, mb, v, Vector.U2, Vector.U1, Vector.U3);
					addFace(t, mb, v, Vector.U3, Vector.U1, Vector.U2);
				}
			}
		}
		
		return mb.build();
	}

	
	private void addFace(Terrain t, MeshBuilder mb, Vector x, Vector f, Vector l, Vector u) {
		byte tx = t.getTerrain(x);
		byte tf = t.getTerrain(x.plus(f));
		
		if ( (tx == 0) != (tf == 0) ) {
			String imageName = tx == 0 ? blockTypes.getImageName(tf) : blockTypes.getImageName(tx); 
			mb.addTri(
				imageName,
				x.plus(f.minus(l).minus(u).times(0.5)), 
				x.plus(f.minus(l).plus(u).times(0.5)),
				x.plus(f.plus(l).plus(u).times(0.5)),
				f,
				new Vector(0, 0, 0, 1.0),
				new Vector(0, 1, 0, 1.0),
				new Vector(1, 1, 0, 1.0)
			);
			mb.addTri(
				imageName,
				x.plus(f.minus(l).minus(u).times(0.5)), 
				x.plus(f.plus(l).plus(u).times(0.5)),
				x.plus(f.plus(l).minus(u).times(0.5)),
				f,
				new Vector(0, 0, 0, 1.0),
				new Vector(1, 1, 0, 1.0),
				new Vector(1, 0, 0, 1.0)
			);
		}
	}

	
}
