package game.main;

import java.util.function.Function;

import game.terrain.TerrainWithTr;
import game.terrain.TerrainWithTrBuilder;
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
import game.model.DefaultCompositeImage;
import game.model.Mesh;
import game.model.Renderable;
import game.model.UniformBindingPool;
import game.terrain.Terrain;
import game.terrain.TerrainBlockBuilder;
import game.terrain.TerrainBlockTypes;
import game.view.View;

public class TerrainBlockArray implements Renderable {

	private static Logger log = LoggerFactory.getLogger(TerrainBlockArray.class);
	
	private int dx = 1;
	private int dy = 1;
	private int dz = 1;
	private int chunkSize = 16;

	private View view;
	private Terrain terrain;
	
	private CompiledMesh[] compiledMesh = new CompiledMesh[dx*dy*dz];

	
	public TerrainBlockArray(Terrain terrain, View view, GLFactory glFactory, UniformBindingPool bindingPool, CompiledMeshProgram meshProgram, Function<String, Image> resourceImageProvider) {
		this.view = view;
		this.terrain = terrain;
		
		TerrainBlockTypes terrainBlockTypes = new TerrainBlockTypes();
		TerrainBlockBuilder terrainBlock = new TerrainBlockBuilder(terrainBlockTypes);
		DefaultCompositeImage terrainCompositeImage = new DefaultCompositeImage(resourceImageProvider);
		terrainCompositeImage.addAll(Lists.newArrayList("marble.jpg"));
		CompiledTexture terrainCompiledTexture = new CompiledTexture(glFactory, terrainCompositeImage);
		
		for(int x=0;x<dx;x++) {
			for(int y=0;y<dy;y++) {
				for(int z=0;z<dz;z++) {
					log.info("Create chunk " + x + " " + y + " " + z);
					Matrix tr = Matrix.translate(new Vector(x, y, z)).times(Matrix.scale(1.0));
					TerrainWithTr terrainWithTr = new TerrainWithTrBuilder().terrain(terrain).tr(tr).build();
					Mesh terrainMesh = terrainBlock.createMesh(terrainWithTr, x, y, z, chunkSize);
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
		int x = (int) Utils.clamp(p.x() / chunkSize, 0, dx);
		int y = (int) Utils.clamp(p.y() / chunkSize, 0, dy);
		int z = (int) Utils.clamp(p.z() / chunkSize, 0, dz);
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
