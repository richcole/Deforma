package game;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import game.controllers.GravityController;
import game.controllers.MarchingCubesPainterController;
import game.controllers.PositionController;
import game.events.ApplicationCloseEvent;
import game.events.Clock;
import game.events.DisplayCloseEvent;
import game.events.EventBus;
import game.geom.HeightMapGeom;
import game.geom.LineGeom;

public class Context implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	private EventBus eventBus;
	private Thread thread;

	public Context() {
		this.eventBus = new EventBus();
		this.thread = new Thread(this);
		thread.setDaemon(true);
		thread.start();
	}
	
	public void join() throws InterruptedException {
	  thread.join();
	}

	public void run() {
    Clock clock = new Clock(eventBus);
		GLDisplay display = new GLDisplay(clock, eventBus);
		
		eventBus.onEventType(display, (e) -> eventBus.post(new ApplicationCloseEvent(eventBus)), DisplayCloseEvent.class);
		
		EventRunner eventRunner = new EventRunner(eventBus);

		SimpleProgram simpleProgram = new SimpleProgram(eventBus);
		ImageTexture marble = new ImageTexture(eventBus, "skyline.jpg");
		ImageTexture grass = new ImageTexture(eventBus, "grass.jpg");
		ImageTexture gradientTexture = new ImageTexture(eventBus, new GradientImage(256, 256));
    ImageTexture greenTexture = new ImageTexture(eventBus, new SolidImage(8, 8, 0, 1.0, 0));
    ImageTexture redTexture = new ImageTexture(eventBus, new SolidImage(8, 8, 1.0, 0, 0));
    ImageTexture blueTexture = new ImageTexture(eventBus, new SolidImage(8, 8, 0, 0, 1.0));

		View view = new View(display, simpleProgram, clock, eventBus);

		
		if ( true ) {
  		MeshContainer meshContainer = new MeshContainer();
  
  		InputProcessor inputProcessor = new InputProcessor(clock, eventBus);
  		PositionController positionController = new PositionController(eventBus, clock, inputProcessor, view);
  		MarchingCubesPainterController mcPainterController = new MarchingCubesPainterController(
  				eventBus, positionController, inputProcessor, simpleProgram,
  				gradientTexture, meshContainer);
      view.add(meshContainer);
  
  		
  		CenterCube centerCube = new CenterCube(eventBus, view, simpleProgram, marble);
  		view.add(centerCube.getModel());
		}

		if (false) {
			List<Geom> girlModelList = new ModelLoader()
					.load(eventBus, "/home/richcole/models/Girl/", marble);
			CompiledMeshList girlModel = new CompiledMeshList(eventBus, simpleProgram,
					girlModelList);
			// ModelController girlModelController = new ModelController(girlModel);

			RenderableModel girl = new RenderableModel(girlModel);
			girl.setModelTr(Matrix.rot(Math.PI / 2, Vector.U1));
			view.add(girl);
		}

		if (false) {
			RenderableModel trees = new RenderableModel(
					new CompiledMeshList(eventBus, simpleProgram, new ModelLoader()
							.load(eventBus, "/home/richcole/models/trees9/trees9.3ds", marble)));
			trees.setModelTr(Matrix.translate(new Vector(20, 0, 0))
					.times(Matrix.rot(Math.PI / 2, Vector.U1)));
			view.add(trees);
		}

		if (false) {
			XPSReader xpsReader = new XPSReader();
			MaterialSource materialSource = new MaterialSource(eventBus);
			XPSModelBuilder xpsBuilder = new XPSModelBuilder(eventBus, materialSource, marble);
			String fname = "src/main/resources/huan_shuyi";
			File root = new File(fname);
			XPSModel xpsBasicModel = xpsReader.read(root, "xps.xps");
			CompiledMeshList xpsModel = xpsBuilder.toCompiledMesh(simpleProgram,
					marble, xpsBasicModel);
			CompiledMesh xpsBoneModel = xpsBuilder.getBoneCompiledMesh(simpleProgram,
					marble, xpsBasicModel);
			// ModelController xpsModelController = new ModelController(xpsModel, ui);

			List<RenderableModel> xpsModels = Lists.newArrayList();
			for (int i = 0; i < 1; ++i) {
				RenderableModel xpsMesh = new RenderableModel(xpsBoneModel);
				xpsMesh.setModelTr(Matrix.translate(Vector.U3.times((i + 1) * 5))
						.times(Matrix.rot(Math.PI, Vector.U1)).times(Matrix.scale(5)));
				xpsModels.add(xpsMesh);
			}
			view.addAll(xpsModels);

		}

		if ( true )
		{
			Matrix tr = Matrix.scale(new Vector(20, 20, 20));
			Matrix ntr = Matrix.scale(new Vector(1/20.0, 1/20.0, 1/20.0, 1.0));
			HeightMap hm = new HeightMap(50, 20, 50, tr, ntr);
			HeightMapGeom hmg = new HeightMapGeom(grass, hm);
			CompiledMesh hmm = new CompiledMesh(eventBus, simpleProgram, hmg);
			// hmm.setWireFrame(true);
			view.add(hmm);
			new GravityController(eventBus, clock, view, hm);
		}

		if ( true ) {
      view.add(new CompiledMesh(eventBus, simpleProgram, new LineGeom(new Box(Vector.Z, Vector.U1.times(5)), 0.2, redTexture)));
      view.add(new CompiledMesh(eventBus, simpleProgram, new LineGeom(new Box(Vector.Z, Vector.U2.times(5)), 0.2, greenTexture)));
      view.add(new CompiledMesh(eventBus, simpleProgram, new LineGeom(new Box(Vector.Z, Vector.U3.times(5)), 0.2, blueTexture)));
		}

		while (!display.isClosed()) {
			clock.run();
			eventBus.processEvents();
		}
	}

	public EventBus getEventBus() {
		return eventBus;
	}
}
