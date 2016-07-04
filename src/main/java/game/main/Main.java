package game.main;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import game.controllers.InputProcessor;
import game.controllers.KeyDownEvent;
import game.controllers.Player;
import game.controllers.PositionController;
import game.controllers.TerrainPlayerController;
import game.creature.Cache;
import game.creature.Creature;
import game.creature.CreatureFactory;
import game.creature.CreatureModel;
import game.creature.CreatureModelFactory;
import game.creature.RandomWalkCreatureBehaviour;
import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.gl.Disposer;
import game.gl.GLDisplay;
import game.gl.GLFactory;
import game.image.CachingImageProvider;
import game.image.ResourceImageProvider;
import game.math.Matrix;
import game.math.Vector;
import game.model.AnimSet;
import game.model.CompiledAnimSet;
import game.model.CompiledMesh;
import game.model.CompiledMeshFrameList;
import game.model.CompiledMeshProgram;
import game.model.CompiledTexture;
import game.model.CompositeImage;
import game.model.Mesh;
import game.model.MeshFactory;
import game.model.MeshFrame;
import game.model.MeshFrameList;
import game.model.PosititionRenderable;
import game.model.UniformBindingPool;
import game.nwn.NwnImageProvider;
import game.nwn.NwnMeshConverter;
import game.nwn.readers.BinaryFileReader;
import game.nwn.readers.ErfReader;
import game.nwn.readers.ErfReader.ErfKeyEntry;
import game.nwn.readers.ErfReaderList;
import game.nwn.readers.KeyReader;
import game.nwn.readers.ResourceType;
import game.terrain.Terrain;
import game.terrain.TerrainBlockBuilder;
import game.terrain.TerrainBlockTypes;
import game.view.View;

public class Main {

	private static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {
		EventBus eventBus = new EventBus();
		Clock clock = new Clock(eventBus);
		Disposer disposer = new Disposer(clock);
		GLFactory glFactory = new GLFactory(disposer, eventBus, clock);
		UniformBindingPool bindingPool = new UniformBindingPool();

		GLDisplay display = glFactory.newDisplay();
		View view = new View(eventBus, clock, display);

		CompiledMeshProgram meshProgram = new CompiledMeshProgram(glFactory);
		CachingImageProvider resourceImageProvider = new CachingImageProvider(
				new ResourceImageProvider());
		CompositeImage compositeImage = new CompositeImage(resourceImageProvider);

		Mesh mesh = new MeshFactory().newSquareMesh(view.getUp().times(-5),
				view.getLeft().times(100), view.getForward().times(100),
				"marble.jpg");

		InputProcessor inputProcessor = new InputProcessor(clock, eventBus);

		Terrain terrain = new Terrain(256, 64, 256);
		terrain.randomize(new Random(), 8, 16);
		
		Player player = new Player(view);
		PositionController positionController = new PositionController(eventBus, clock, inputProcessor, player);
		TerrainPlayerController terrainPlayerController = new TerrainPlayerController(eventBus, clock, player, terrain);
		player.setTerrainPlayerController(terrainPlayerController);
		
		compositeImage.addAll(mesh.imageList);
		CompiledTexture compiledTexture = new CompiledTexture(glFactory,
				compositeImage, true);
		CompiledMesh compiledMesh = new CompiledMesh(glFactory, bindingPool,
				meshProgram, compiledTexture, mesh);
		view.add(new PosititionRenderable(compiledMesh, Matrix.IDENTITY));

		/*


		File nwnRoot = new File("/Users/richard/nwn");
		if ( ! nwnRoot.exists() ) {
			nwnRoot = new File("/Users/richcole/nwn");
		}
		ErfReaderList erfReaderList = new ErfReaderList();
		erfReaderList.add(new File(nwnRoot, "texturepacks/Textures_Tpa.erf"));
		erfReaderList.add(new File(nwnRoot, "texturepacks/xp1_tex_tpa.erf"));
		erfReaderList.add(new File(nwnRoot, "texturepacks/xp2_tex_tpa.erf"));
		
		KeyReader keyReader = new KeyReader(nwnRoot);
		CachingImageProvider nwnImageProvider = new CachingImageProvider(new NwnImageProvider(erfReaderList, keyReader));
		Matrix tr;

		CreatureModelFactory creatureModelFactory = new CreatureModelFactory(glFactory, bindingPool, meshProgram, view, eventBus, clock, inputProcessor, nwnImageProvider, keyReader);
		Cache<CreatureModel> creatureModelFactoryCache = new Cache<CreatureModel>(creatureModelFactory);
		CreatureFactory creatureFactory = new CreatureFactory(clock, view, eventBus, creatureModelFactoryCache);
		
		List<Creature> creatureList = Lists.newArrayList();
		Vector pos;
		Creature c;
		for(int i=0;i<1;++i) {
			pos = view.getForward().times(i*5).plus(view.getLeft().times(5));
			c = creatureFactory.createCreature("c_wererat", "cwalk", pos, 0);
			c.setCreatureBehaviour(new RandomWalkCreatureBehaviour(c));
			creatureList.add(c);

			pos = view.getForward().times(i*5).plus(view.getLeft().times(10));
			c = creatureFactory.createCreature("c_manticore", "cwalk", pos, 0);
			c.setCreatureBehaviour(new RandomWalkCreatureBehaviour(c));
			creatureList.add(c);
		}
		*/
		
		TerrainBlockArray terrainBlockArray = new TerrainBlockArray(terrain, view, glFactory, bindingPool, meshProgram, resourceImageProvider);
		view.add(terrainBlockArray);
		
		while (!display.isClosed()) {
			clock.tick();
		}
	}

}
