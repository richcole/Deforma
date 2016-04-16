package game.main;

import java.io.File;
import java.util.Map.Entry;

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
		View view = new View(eventBus, clock, display);

		CompiledMeshProgram meshProgram = new CompiledMeshProgram(glFactory);
		CachingImageProvider resourceImageProvider = new CachingImageProvider(new ResourceImageProvider());
		CompositeImage compositeImage = new CompositeImage(resourceImageProvider);
		
		Mesh mesh = new MeshFactory().newSquareMesh(view.getUp().times(-5), view.getLeft().times(100),
				view.getForward().times(100), "marble.jpg");
		
		compositeImage.addAll(mesh.imageList);
		
		CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage);
		CompiledMesh compiledMesh = new CompiledMesh(glFactory, bindingPool, meshProgram, compiledTexture, mesh);
		view.add(new PosititionRenderable(compiledMesh, Matrix.IDENTITY));

		InputProcessor inputProcessor = new InputProcessor(clock, eventBus);
		PositionController positionController = new PositionController(eventBus, clock, inputProcessor, view);

		loadRat2(glFactory, bindingPool, meshProgram, view, eventBus, clock, inputProcessor);
		
		while (!display.isClosed()) {
			clock.tick();
		}
	}

	private static void loadRat2(GLFactory glFactory, UniformBindingPool bindingPool, CompiledMeshProgram program,
			View view, EventBus eventBus, Clock clock, InputProcessor inputProcessor) {
		KeyReader keyReader = new KeyReader(new File("/Users/richcole/nwn"));
		CachingImageProvider nwnImageProvider = new CachingImageProvider(new NwnImageProvider(keyReader));
		
		AnimSet animSet = new NwnMeshConverter().convertToMeshFrameList(keyReader.getModel("c_wererat"));
	    CompositeImage compositeImage = new CompositeImage(nwnImageProvider);
	    
	    for(Entry<String, MeshFrameList> animEntry: animSet) {
	    	for(MeshFrame meshFrame: animEntry.getValue().getMeshFrameList()) {
	    		compositeImage.addAll(meshFrame.mesh.imageList);
	    	}
	    }
	    CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage);
	    CompiledAnimSet compiledAnimSet = new CompiledAnimSet();
	    
	    for(Entry<String, MeshFrameList> animEntry: animSet) {
	    	String animName = animEntry.getKey();
	    	MeshFrameList meshFrameList = animEntry.getValue();
		    CompiledMeshFrameList compiledMeshFrameList = new CompiledMeshFrameList(meshFrameList.getTotalFrameTime());
		    for(MeshFrame meshFrame: meshFrameList.getMeshFrameList()) {
		    	CompiledMesh compiledMesh = new CompiledMesh(glFactory, bindingPool, program, compiledTexture, meshFrame.mesh);
		    	compiledMeshFrameList.add(meshFrame.frame, compiledMesh);
		    }
		    compiledAnimSet.put(animName, compiledMeshFrameList);
	    }

	    Matrix tr = Matrix.IDENTITY;
		tr = tr.times(Matrix.translate(view.getForward().times(5)));
		tr = tr.times(Matrix.rot(Math.PI / 2, view.getLeft()));
		view.add(new PosititionRenderable(compiledAnimSet, tr));

		eventBus.onEvent(inputProcessor, KeyDownEvent.class, (e) -> { 
			if ( e.key == Keyboard.KEY_P ) 
				compiledAnimSet.nextAnim(); 
		});
		
		eventBus.onEvent(clock, TickEvent.class, (e) -> compiledAnimSet.advanceSeconds(e.dt));
	}
}
