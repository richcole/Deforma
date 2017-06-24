package component.gl;

import game.events.Clock;
import game.events.EventBus;
import game.gl.Disposer;
import game.gl.GLDisplay;
import game.gl.GLFactory;
import game.gl.GLTexture;
import game.image.CachingImageProvider;
import game.image.ResourceImageProvider;
import game.model.CompiledMesh;
import game.model.CompiledMeshProgram;
import game.model.CompiledTexture;
import game.model.DefaultCompositeImage;
import game.model.Mesh;
import game.model.TextureCompositeImage;
import game.model.UniformBindingPool;
import lombok.Getter;
import org.lwjgl.opengl.Display;

public class GlEngine {
	private EventBus eventBus = new EventBus();
	private Clock clock = new Clock(eventBus);
	private Disposer disposer = new Disposer(clock);
	private GLFactory glFactory = new GLFactory(disposer, eventBus, clock);
	private UniformBindingPool bindingPool = new UniformBindingPool();
	private GLDisplay display = glFactory.newDisplay();

	private CompiledMeshProgram meshProgram = new CompiledMeshProgram(glFactory);
	private CachingImageProvider resourceImageProvider = new CachingImageProvider(new ResourceImageProvider());

	@Getter
	private boolean isClosed = false;

	public CompiledMesh compileMesh(Mesh mesh) {
		DefaultCompositeImage compositeImage = new DefaultCompositeImage(resourceImageProvider);
		compositeImage.addAll(mesh.imageList);
		CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage);
		return new CompiledMesh(glFactory, bindingPool, meshProgram, compiledTexture, mesh);
	}

	public CompiledMesh compileMesh(Mesh mesh, GLTexture glTexture) {
		TextureCompositeImage textureCompositeImage = new TextureCompositeImage(glTexture);
		CompiledTexture compiledTexture = new CompiledTexture(glFactory, textureCompositeImage);
		return new CompiledMesh(glFactory, bindingPool, meshProgram, compiledTexture, mesh);
	}

	public double getHeightToWidthRatio() {
		return ((double) Display.getHeight()) / Display.getWidth();
	}

	public boolean checkForClosed() {
		if (Display.isCloseRequested()) {
			isClosed = true;
		}
		return isClosed;
	}

	public Disposer getDisposer() {
		return disposer;
	}
}
