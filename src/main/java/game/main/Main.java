package game.main;

import game.controllers.InputProcessor;
import game.controllers.PositionController;
import game.events.Clock;
import game.events.EventBus;
import game.gl.Disposer;
import game.gl.GLDisplay;
import game.gl.GLFactory;
import game.image.CachingImageProvider;
import game.image.Image;
import game.image.ResourceImageProvider;
import game.math.Vector;
import game.model.CompiledMesh;
import game.model.CompiledMeshProgram;
import game.model.CompiledTexture;
import game.model.CompositeImage;
import game.model.Mesh;
import game.model.MeshCompiler;
import game.model.MeshFactory;
import game.model.PosititionRenderable;
import game.nwn.NwnImageProvider;
import game.nwn.NwnMeshConverter;
import game.nwn.readers.KeyReader;
import game.view.View;

import java.io.File;
import java.util.function.Function;

public class Main {

  public static void main(String[] args) {
    EventBus eventBus = new EventBus();
    Clock clock = new Clock(eventBus);
    Disposer disposer = new Disposer(clock);
    GLFactory glFactory = new GLFactory(disposer, eventBus, clock);

    GLDisplay display = glFactory.newDisplay();
    CompiledMeshProgram program = new CompiledMeshProgram(glFactory);
    View view = new View(eventBus, clock, display, program);
    
    CachingImageProvider resourceImageProvider = new CachingImageProvider(new ResourceImageProvider());
	MeshCompiler meshCompiler = new MeshCompiler(glFactory, program, resourceImageProvider);
    
    Mesh mesh = new MeshFactory().newSquareMesh(view.getUp().times(-5), view.getLeft().times(100), view.getForward().times(100), "marble.jpg");
    view.add(meshCompiler.compile(mesh, Vector.Z));

    InputProcessor inputProcessor = new InputProcessor(clock, eventBus);
    PositionController positionController = new PositionController(eventBus, clock, inputProcessor, view);
    
    KeyReader keyReader = new KeyReader(new File("/Users/richcole/nwn"));
    CachingImageProvider nwnImageProvider = new CachingImageProvider(new NwnImageProvider(keyReader));
    meshCompiler = new MeshCompiler(glFactory, program, nwnImageProvider);
    mesh = new NwnMeshConverter().convert(keyReader.getModel("c_wererat"));
    view.add(meshCompiler.compile(mesh, view.getForward().times(5)));
    
    while(! display.isClosed()) {
      clock.tick();
    }
  }

}
