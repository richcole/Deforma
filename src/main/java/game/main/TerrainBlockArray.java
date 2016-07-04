package game.main;

import java.util.Random;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import game.gl.GLFactory;
import game.image.Image;
import game.math.Matrix;
import game.math.Utils;
import game.math.Vector;
import game.model.CompiledMesh;
import game.model.CompiledMeshProgram;
import game.model.CompiledTexture;
import game.model.CompositeImage;
import game.model.Mesh;
import game.model.Renderable;
import game.model.UniformBindingPool;
import game.terrain.Terrain;
import game.terrain.TerrainBlockBuilder;
import game.terrain.TerrainBlockTypes;
import game.view.View;

public class TerrainBlockArray implements Renderable {

	private static Logger log = LoggerFactory.getLogger(TerrainBlockArray.class);
	
	private int dx = 8;
	private int dy = 2;
	private int dz = 8;
	private int blockSize = 16;

	private View view;
	private Terrain terrain;
	
	private CompiledMesh[] compiledMesh = new CompiledMesh[dx*dy*dz];

	
	public TerrainBlockArray(Terrain terrain, View view, GLFactory glFactory, UniformBindingPool bindingPool, CompiledMeshProgram meshProgram, Function<String, Image> resourceImageProvider) {
		this.view = view;
		this.terrain = terrain;
		
		TerrainBlockTypes terrainBlockTypes = new TerrainBlockTypes();
		TerrainBlockBuilder terrainBlock = new TerrainBlockBuilder(terrainBlockTypes, Matrix.IDENTITY);
		CompositeImage terrainCompositeImage = new CompositeImage(resourceImageProvider);
		terrainCompositeImage.addAll(Lists.newArrayList("marble.jpg"));
		CompiledTexture terrainCompiledTexture = new CompiledTexture(glFactory, terrainCompositeImage, true);
		
		for(int x=0;x<dx;x++) {
			for(int y=0;y<dy;y++) {
				for(int z=0;z<dz;z++) {
					log.info("Create block " + x + " " + y + " " + z);
					Mesh terrainMesh = terrainBlock.createMesh(terrain, new Vector(x, y, z).times(blockSize), blockSize);
					compiledMesh[index(x,y,z)] = new CompiledMesh(glFactory, bindingPool, meshProgram, terrainCompiledTexture, terrainMesh);
				}
			}
		}
	}

	private int index(int x, int y, int z) {
		return (x * dy * dz) + (y * dz) + z;
	}

	@Override
	public void render(Matrix viewMatrix) {
		Vector p = view.getPosition();
		int x = (int) Utils.clamp(p.x() / blockSize, 0, dx);
		int y = (int) Utils.clamp(p.y() / blockSize, 0, dy);
		int z = (int) Utils.clamp(p.z() / blockSize, 0, dz);
		for(int sx=-1;sx<=1;++sx) {
			for(int sy=-1;sy<=1;++sy) {
				for(int sz=-1;sz<=1;++sz) {
					renderBlock(x+sx, y+sy, z+sz, viewMatrix);
				}
			}
		}
	}

	private void renderBlock(int x, int y, int z, Matrix viewMatrix) {
		if ( x >= 0 && x < dx && y >= 0 && y < dy && z >= 0 && z < dz ) {
			compiledMesh[index(x, y, z)].render(viewMatrix, Matrix.IDENTITY);
		}
	}
}
