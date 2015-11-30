package game;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import game.events.Clock;
import game.events.EventBus;
import game.ui.UI;
import game.ui.UI;

public class Context implements Runnable {

  private static final Logger log = LoggerFactory.getLogger(Context.class);

  private EventBus eventBus;
  private Thread thread;
  private UI ui;

  public Context(UI ui) {
    this.ui = ui;
    this.eventBus = new EventBus();
    thread = new Thread(this);
    thread.setDaemon(true);
    thread.start();
  }

  public void run() {
    GLDisplay display = new GLDisplay();
    
    Clock clock = new Clock(eventBus);
    CloseWatcher closeWatcher = new CloseWatcher(clock, this, eventBus);
    Simulator simulator = new Simulator(clock, eventBus);

    SimpleProgram simpleProgram = new SimpleProgram();
    DisplayResizer dispayResizer = new DisplayResizer(clock, eventBus);
    ImageTexture marble = new ImageTexture("skyline.jpg");
    ImageTexture gradientTexture = new ImageTexture(new GradientImage(256, 256));
    TriangleMesh triangle = new TriangleMesh(simpleProgram, gradientTexture);

    View view = new View(simpleProgram, clock, eventBus);

    MeshContainer meshContainer = new MeshContainer();

    InputProcessor inputProcessor = new InputProcessor(clock, eventBus);
    PositionController positionController = new PositionController(eventBus, simulator, inputProcessor, view);
    MarchingCubesPainterController mcPainterController = new MarchingCubesPainterController
        (eventBus, positionController, inputProcessor, simpleProgram, gradientTexture, meshContainer);

    if ( false ) {
      List<Geom> girlModelList = new ModelLoader().load(
          "/home/richcole/models/Girl/", marble);
      CompiledMeshList girlModel = new CompiledMeshList(simpleProgram,
          girlModelList);
      ModelController girlModelController = new ModelController(girlModel, ui);
  
      RenderableModel girl = new RenderableModel(girlModel);
      girl.setModelTr(Matrix.rot(Math.PI / 2, Vector.U1));
  
      RenderableModel trees = new RenderableModel(new CompiledMeshList(
          simpleProgram, new ModelLoader().load(
              "/home/richcole/models/trees9/trees9.3ds", marble)));
      trees.setModelTr(Matrix.translate(new Vector(20, 0, 0)).times(
          Matrix.rot(Math.PI / 2, Vector.U1)));

      XPSReader xpsReader = new XPSReader();
      MaterialSource materialSource = new MaterialSource();
      XPSModelBuilder xpsBuilder = new XPSModelBuilder(materialSource, marble);
      String fname = "/home/richcole/models/";
      File root = new File(fname);
      XPSModel xpsBasicModel = xpsReader.read(root, "xps.xps");
      CompiledMeshList xpsModel = xpsBuilder.toCompiledMesh(simpleProgram,
          marble, xpsBasicModel);
      CompiledMesh xpsBoneModel = xpsBuilder.getBoneCompiledMesh(simpleProgram,
          marble, xpsBasicModel);
      ModelController xpsModelController = new ModelController(xpsModel, ui);

      List<RenderableModel> xpsModels = Lists.newArrayList();
      for (int i = 0; i < 1; ++i) {
        RenderableModel xpsMesh = new RenderableModel(xpsBoneModel);
        xpsMesh.setModelTr(Matrix.translate(Vector.U3.times((i + 1) * 5))
            .times(Matrix.rot(Math.PI, Vector.U1)).times(Matrix.scale(5)));
        xpsModels.add(xpsMesh);
      }
      
      view.add(girl);
      view.add(trees);
      view.addAll(xpsModels);
    }

    LineGeom line = new LineGeom(new Box(Vector.U1.times(-5),
        Vector.U2.times(5)), 0.2, marble);

    CompiledMesh lineMesh = new CompiledMesh(simpleProgram, line);

    view.add(triangle);
    view.add(meshContainer);
    view.add(lineMesh);

    while(! closeWatcher.isClosed()) {
      clock.run();
      eventBus.processEvents();
    }
  }

  public EventBus getEventBus() {
    return eventBus;
  }
}
