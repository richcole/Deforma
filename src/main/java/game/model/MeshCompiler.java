package game.model;

import java.util.function.Function;

import game.gl.GLFactory;
import game.image.Image;
import game.image.ResourceImageProvider;
import game.math.Vector;

public class MeshCompiler {

	private GLFactory glFactory;
	private CompiledMeshProgram program;
	private Function<String, Image> imageProvider;
	
	public MeshCompiler(GLFactory glFactory, CompiledMeshProgram program, Function<String, Image> imageProvider) {
		super();
		this.glFactory = glFactory;
		this.program = program;
		this.imageProvider = imageProvider;
	}

	public PosititionRenderable compile(Mesh mesh, Vector position) {
	    CompositeImage compositeImage = new CompositeImage(mesh.imageList, imageProvider);
	    CompiledTexture compiledTexture = new CompiledTexture(glFactory, compositeImage);
	    CompiledMesh compiledMesh = new CompiledMesh(glFactory, program, compiledTexture, mesh);
	    return new PosititionRenderable(compiledMesh, position);
	}
}
