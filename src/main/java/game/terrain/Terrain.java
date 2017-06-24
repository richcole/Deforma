package game.terrain;

import java.util.Random;

import game.math.Vector;

public class Terrain {
	
	private byte blockType[];
	private int dx;
	private int dy;
	private int dz;
	
	public Terrain(int dx, int dy, int dz) {
		this.dx = dx;
		this.dy = dy;
		this.dz = dz;
		blockType = new byte[dx*dy*dz];
	}
	
	public void randomize(Random random, int avgHeight, int deviation) {

		double[] height = new double[dx*dz];
		for(int x=0;x<dx;++x) {
			for(int z=0;z<dz;++z) {
				height[heightIndex(x,z)] = avgHeight + random.nextInt(deviation) - (deviation / 2.0);
			}
		}

		// smooth the terrain
		for(int i=0;i<5;++i) {
			for(int x=0;x<dx;++x) {
				for(int z=0;z<dz;++z) {
					int count = 0;
					double h = 0;
					for(int sx=-1;sx<=1;++sx) {
						for(int sz=-1;sz<=1;++sz) {
							if ( x+sx >= 0 && x+sx < dx && z+sz >= 0 && z+sz < dz) {
								h += height[heightIndex(x+sx,z+sz)];
								count += 1;
							}
						}						
					}
					height[heightIndex(x,z)] = h / count;
				}
			}
		}

		for(int x=0;x<dx;++x) {
			for(int z=0;z<dz;++z) {
				for(int y=0;y<dy;++y) {
					int index = getIndex(x, y, z); 
					blockType[index] = (byte)(y < height[heightIndex(x,z)] ? 1 : 0);
				}
			}
		}
	}
	
	private int heightIndex(int x, int z) {
		return x*dz+z;
	}

	public byte getTerrain(Vector x) {
		int index = getIndex(x);
		if ( index >= 0 && index < blockType.length ) {
			return blockType[index];
		}
		return 0;
	}

	public byte getTerrain(int x, int y, int z) {
		int index = getIndex(x, y, z);
		if ( index >= 0 && index < blockType.length ) {
			return blockType[index];
		}
		return 0;
	}

	private int getIndex(Vector x) {
		return getIndex((int)Math.floor(x.x()), (int)Math.floor(x.y()), (int)Math.floor(x.z()));
	}
	
	private int getIndex(int x, int y, int z) {
		return (int)(x * dy * dz + y * dz + z);
	}

	public boolean insideTerrain(Vector p) {
		return insideTerrain((int)p.x(), (int)p.y(), (int)p.z());
	}

	private boolean insideTerrain(int x, int y, int z) {
		return x >= 0 && y >= 0 && z >= 0 && x < dx && y < dy && z < dz;
	}

	public int getDx() {
		return dx;
	}

	public int getDy() {
		return dy;
	}

	public int getDz() {
		return dz;
	}
}
