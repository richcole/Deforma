package game.model;

import java.util.function.Function;

import game.gl.GLFactory;
import game.image.Image;
import game.image.ResourceImageProvider;
import game.math.Matrix;
import game.math.Vector;

public class AnimMeshCompiler {

	private GLFactory glFactory;
	private CompiledAnimMeshProgram program;
	private Function<String, Image> imageProvider;
	private UniformBindingPool bindingPool;
	
	public AnimMeshCompiler(GLFactory glFactory, UniformBindingPool bindingPool, CompiledAnimMeshProgram program, Function<String, Image> imageProvider) {
		super();
		this.glFactory = glFactory;
		this.program = program;
		this.imageProvider = imageProvider;
		this.bindingPool = bindingPool;
	}

	public PosititionRenderable compile(AnimMesh mesh, Matrix tr) {
	    CompositeImage compositeImage = new CompositeImage(mesh.imageList, imageProvider);
	    CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage);
	    CompiledAnimMesh compiledMesh = new CompiledAnimMesh(glFactory, bindingPool, program, compiledTexture, mesh);
	    return new PosititionRenderable(compiledMesh, tr);
	}
}
