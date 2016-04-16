package game.main;

import java.io.File;

import org.lwjgl.input.Keyboard;

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
import game.model.AnimMesh;
import game.model.AnimMeshCompiler;
import game.model.CompiledAnimMeshProgram;
import game.model.CompiledMesh;
import game.model.CompiledMeshFrameList;
import game.model.CompiledMeshProgram;
import game.model.CompiledTexture;
import game.model.CompositeImage;
import game.model.MeshFactory;
import game.model.MeshFrame;
import game.model.MeshFrameList;
import game.model.PosititionRenderable;
import game.model.UniformBindingPool;
import game.nwn.NwnImageProvider;
import game.nwn.NwnMeshConverter;
import game.nwn.readers.KeyReader;
import game.view.View;

public class Main {

	public static void main(String[] args) {
		EventBus eventBus = new EventBus();
		Clock clock = new Clock(eventBus);
		Disposer disposer = new Disposer(clock);
		GLFactory glFactory = new GLFactory(disposer, eventBus, clock);
		UniformBindingPool bindingPool = new UniformBindingPool();

		GLDisplay display = glFactory.newDisplay();
		CompiledAnimMeshProgram animMeshProgram = new CompiledAnimMeshProgram(glFactory);
		View view = new View(eventBus, clock, display, animMeshProgram);

		CachingImageProvider resourceImageProvider = new CachingImageProvider(new ResourceImageProvider());
		AnimMeshCompiler meshCompiler = new AnimMeshCompiler(glFactory, bindingPool, animMeshProgram, resourceImageProvider);

		AnimMesh mesh = new MeshFactory().newSquareAnimMesh(view.getUp().times(-5), view.getLeft().times(100),
				view.getForward().times(100), "marble.jpg");
		view.add(meshCompiler.compile(mesh, Matrix.IDENTITY));

		InputProcessor inputProcessor = new InputProcessor(clock, eventBus);
		PositionController positionController = new PositionController(eventBus, clock, inputProcessor, view);

		CompiledMeshProgram meshProgram = new CompiledMeshProgram(glFactory);
		loadRat2(glFactory, bindingPool, meshProgram, view, eventBus, clock, inputProcessor);
		
		while (!display.isClosed()) {
			clock.tick();
		}
	}

	private static void loadRat(GLFactory glFactory, UniformBindingPool bindingPool, CompiledAnimMeshProgram program,
			View view) {
		KeyReader keyReader = new KeyReader(new File("/Users/richcole/nwn"));
		CachingImageProvider nwnImageProvider = new CachingImageProvider(new NwnImageProvider(keyReader));
		AnimMeshCompiler meshCompiler = new AnimMeshCompiler(glFactory, bindingPool, program, nwnImageProvider);
		AnimMesh mesh = new NwnMeshConverter().convertToAnimMesh(keyReader.getModel("c_wererat"));
		Matrix tr = Matrix.IDENTITY;
		tr = tr.times(Matrix.translate(view.getForward().times(5)));
		tr = tr.times(Matrix.rot(Math.PI / 2, view.getLeft()));
		view.add(meshCompiler.compile(mesh, tr));
	}

	private static void loadRat2(GLFactory glFactory, UniformBindingPool bindingPool, CompiledMeshProgram program,
			View view, EventBus eventBus, Clock clock, InputProcessor inputProcessor) {
		KeyReader keyReader = new KeyReader(new File("/Users/richcole/nwn"));
		CachingImageProvider nwnImageProvider = new CachingImageProvider(new NwnImageProvider(keyReader));
		MeshFrameList meshFrameList = new NwnMeshConverter().convertToMeshFrameList(keyReader.getModel("c_wererat"), "creadyl");
	    CompositeImage compositeImage = new CompositeImage(meshFrameList.getMeshFrameList().get(0).mesh.imageList, nwnImageProvider);
	    CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage);
	    
	    CompiledMeshFrameList compiledMeshFrameList = new CompiledMeshFrameList(meshFrameList.getTotalFrameTime());
	    for(MeshFrame meshFrame: meshFrameList.getMeshFrameList()) {
	    	CompiledMesh compiledMesh = new CompiledMesh(glFactory, bindingPool, program, compiledTexture, meshFrame.mesh);
	    	compiledMeshFrameList.add(meshFrame.frame, compiledMesh);
	    }

	    Matrix tr = Matrix.IDENTITY;
		tr = tr.times(Matrix.translate(view.getForward().times(5)));
		tr = tr.times(Matrix.rot(Math.PI / 2, view.getLeft()));
		view.add(new PosititionRenderable(compiledMeshFrameList, tr));

		eventBus.onEvent(inputProcessor, KeyDownEvent.class, (e) -> { 
			if ( e.key == Keyboard.KEY_P ) 
				compiledMeshFrameList.nextFrame(); 
		});
		
		eventBus.onEvent(clock, TickEvent.class, (e) -> compiledMeshFrameList.advanceSeconds(e.dt));
	}
}
