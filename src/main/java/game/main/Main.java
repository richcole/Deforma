package game.main;

import java.io.File;
import java.lang.reflect.GenericArrayType;
import java.util.Collection;
import java.util.Map.Entry;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.controllers.InputProcessor;
import game.controllers.KeyDownEvent;
import game.controllers.PositionController;
import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.gl.Disposer;
import game.gl.GLDisplay;
import game.gl.GLFactory;
import game.image.CachingImageProvider;
import game.image.ResourceImageProvider;
import game.math.Matrix;
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
		CompositeImage compositeImage = new CompositeImage(
				resourceImageProvider);

		Mesh mesh = new MeshFactory().newSquareMesh(view.getUp().times(-5),
				view.getLeft().times(100), view.getForward().times(100),
				"marble.jpg");

		compositeImage.addAll(mesh.imageList);

		CompiledTexture compiledTexture = new CompiledTexture(glFactory,
				compositeImage, true);
		CompiledMesh compiledMesh = new CompiledMesh(glFactory, bindingPool,
				meshProgram, compiledTexture, mesh);
		view.add(new PosititionRenderable(compiledMesh, Matrix.IDENTITY));

		InputProcessor inputProcessor = new InputProcessor(clock, eventBus);
		PositionController positionController = new PositionController(
				eventBus, clock, inputProcessor, view);

		File nwnRoot = new File("/Users/richard/nwn");
		ErfReaderList erfReaderList = new ErfReaderList();
		erfReaderList.add(new File(nwnRoot, "texturepacks/Textures_tpa.erf"));
		erfReaderList.add(new File(nwnRoot, "texturepacks/xp1_tex_tpa.erf"));
		erfReaderList.add(new File(nwnRoot, "texturepacks/xp2_tex_tpa.erf"));

		/*
		erfReaderList.addAll(nwnRoot);
		
		for(ErfReader reader: erfReaderList.getReaders()) {
			for(String key: reader.getKeys()) {
				if ( key.contains("c_manticore") ) {
					log.info("key '" + key + "'");
				}
			}
			if ( reader.getKeys().contains("c_manticore") ) {
				log.info("Found c_manticore resource");
				Collection<ErfKeyEntry> entryList = reader.getEntryList("c_manticore");
				for(ErfKeyEntry entry: entryList) {
					log.info("entry " + entry.name + " " + entry.resType);
				}
			}
		}
		*/
		
		// erfReaderList.addAll(nwnRoot);
		byte[] manticore = erfReaderList.getResource("c_manticore", ResourceType.DDS);
		KeyReader keyReader = new KeyReader(nwnRoot);
		CachingImageProvider nwnImageProvider = new CachingImageProvider(new NwnImageProvider(erfReaderList, keyReader));
		Matrix tr;

		tr = Matrix.IDENTITY;
		tr = tr.times(Matrix.translate(view.getForward().times(5)));
		tr = tr.times(Matrix.rot(Math.PI / 2, view.getLeft()));
		loadModel(glFactory, bindingPool, meshProgram, view, eventBus, clock,
				inputProcessor, "c_wererat", tr, nwnImageProvider, keyReader);

		tr = Matrix.IDENTITY;
		tr = tr.times(Matrix.translate(view.getForward().times(10)));
		tr = tr.times(Matrix.rot(Math.PI / 2, view.getLeft()));
		loadModel(glFactory, bindingPool, meshProgram, view, eventBus, clock,
				inputProcessor, "c_manticore", tr, nwnImageProvider, keyReader);

		while (!display.isClosed()) {
			clock.tick();
		}
	}

	private static void loadModel(GLFactory glFactory,
			UniformBindingPool bindingPool, CompiledMeshProgram program,
			View view, EventBus eventBus, Clock clock,
			InputProcessor inputProcessor, String modelName, Matrix tr,
			CachingImageProvider imageProvider,
			KeyReader keyReader) 
	{
		;

		AnimSet animSet = new NwnMeshConverter()
				.convertToMeshFrameList(keyReader.getModel(modelName));
		CompositeImage compositeImage = new CompositeImage(imageProvider);

		for (Entry<String, MeshFrameList> animEntry : animSet) {
			for (MeshFrame meshFrame : animEntry.getValue().getMeshFrameList()) {
				compositeImage.addAll(meshFrame.mesh.imageList);
			}
		}
		CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage, false);
		CompiledAnimSet compiledAnimSet = new CompiledAnimSet();

		for (Entry<String, MeshFrameList> animEntry : animSet) {
			String animName = animEntry.getKey();
			log.info("animName=" + animName);
			MeshFrameList meshFrameList = animEntry.getValue();
			CompiledMeshFrameList compiledMeshFrameList = new CompiledMeshFrameList(
					meshFrameList.getTotalFrameTime());
			for (MeshFrame meshFrame : meshFrameList.getMeshFrameList()) {
				CompiledMesh compiledMesh = new CompiledMesh(glFactory,
						bindingPool, program, compiledTexture, meshFrame.mesh);
				compiledMeshFrameList.add(meshFrame.frame, compiledMesh);
			}
			compiledAnimSet.put(animName, compiledMeshFrameList);
		}

		view.add(new PosititionRenderable(compiledAnimSet, tr));

		eventBus.onEvent(inputProcessor, KeyDownEvent.class, (e) -> {
			if (e.key == Keyboard.KEY_P)
				compiledAnimSet.nextAnim();
		});

		eventBus.onEvent(clock, TickEvent.class,
				(e) -> compiledAnimSet.advanceSeconds(e.dt));
	}
}
