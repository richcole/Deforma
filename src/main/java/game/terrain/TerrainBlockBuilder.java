package game.terrain;

import game.math.Matrix;
import game.math.Vector;
import game.model.Mesh;
import game.model.MeshBuilder;

public class TerrainBlockBuilder {
	
	private TerrainBlockTypes blockTypes;

	public TerrainBlockBuilder(TerrainBlockTypes blockTypes) {
		this.blockTypes = blockTypes;
	}
	
	public Mesh createMesh(TerrainWithTr t, int px, int py, int pz, int chunkSize) {
		MeshBuilder mb = new MeshBuilder();
		Matrix tr = t.getTr();
		mb.setTr(tr);
		for(int x=0;x<chunkSize;++x) {
			for(int y=0;y<chunkSize;++y) {
				for(int z=0;z<chunkSize;++z) {
					Vector lowerLeft = new Vector(px + x, py + y, pz + z);
					Vector upperRight = lowerLeft.plus(Vector.ONES);
					Vector lowerLeftTr = tr.times(lowerLeft);
					Vector dv = tr.times(upperRight).minus(lowerLeftTr);
					addFace(t.getTerrain(), mb, lowerLeft, lowerLeftTr, dv, 0, 1, 2);
					addFace(t.getTerrain(), mb, lowerLeft, lowerLeftTr, dv, 1, 0, 2);
					addFace(t.getTerrain(), mb, lowerLeft, lowerLeftTr, dv, 2, 0, 1);
				}
			}
		}
		
		return mb.build();
	}

	private void addFace(Terrain t, MeshBuilder mb, Vector lowerLeft, Vector lowerLeftTr, Vector dv, int f, int u, int l) {
		byte tx = t.getTerrain(lowerLeft);
		byte tf = t.getTerrain(lowerLeft.plus(Vector.UNIT(f)));
		
		if ( (tx == 0) != (tf == 0) ) {
			String imageName = tx == 0 ? blockTypes.getImageName(tf) : blockTypes.getImageName(tx); 
			mb.addTri(
				imageName,
				lowerLeftTr.plus(dv.project(f)),
				lowerLeftTr.plus(dv.project(f).plus(dv.project(u))),
				lowerLeftTr.plus(dv.project(f).plus(dv.project(u)).plus(dv.project(l))),
				Vector.UNIT(f),
				new Vector(0, 0, 0, 1.0),
				new Vector(0, 1, 0, 1.0),
				new Vector(1, 1, 0, 1.0)
			);
			mb.addTri(
				imageName,
				lowerLeftTr.plus(dv.project(f)),
				lowerLeftTr.plus(dv.project(f).plus(dv.project(u)).plus(dv.project(l))),
				lowerLeftTr.plus(dv.project(f).plus(dv.project(l))),
				Vector.UNIT(f),
				new Vector(0, 0, 0, 1.0),
				new Vector(1, 1, 0, 1.0),
				new Vector(1, 0, 0, 1.0)
			);
		}
	}

}
