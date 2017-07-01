package component;

import component.gl.GlEngine;
import game.gl.GLTexture;
import game.math.Matrix;
import game.math.Vector;
import game.model.CompiledMesh;
import game.model.Mesh;
import game.model.MeshFactory;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ComponentBuilder {

	private final GlEngine glEngine;

	private RenderComponent component;
	private Vector position = Vector.Z;
	private Vector scale = Vector.ONES;
	private GLTexture texture;

	public ComponentBuilder newSquare(Vector right, Vector up) {
		Mesh mesh = new MeshFactory().newSquareMesh(Vector.Z, right, up, "marble.jpg");
		CompiledMesh compiledMesh;
		if (texture != null) {
			compiledMesh = glEngine.compileMesh(mesh, texture);
		}
		else {
			compiledMesh = glEngine.compileMesh(mesh);
		}
		this.component = new MeshComponent(Transform.IDENTITY, compiledMesh);
		return this;
	}

	public ComponentBuilder newCube() {
		Mesh mesh = new MeshFactory().newCubeMesh("marble.jpg");
		this.component = new MeshComponent(Transform.IDENTITY, glEngine.compileMesh(mesh));
		return this;
	}

	public ComponentBuilder withPosition(Vector position) {
		this.position = position;
		return this;
	}

	public ComponentBuilder withScale(Vector scale) {
		this.scale = scale;
		return this;
	}

	public ComponentBuilder withScale(double x, double y, double z) {
		this.scale = new Vector(x, y, z, 1.0);
		return this;
	}

	public RenderComponent build() {
		component.setLocalTransform(new DefaultTransform(Matrix.translate(position).times(Matrix.scale(scale))));
		return component;
	}

	public ComponentBuilder withTexture(GLTexture texture) {
		this.texture = texture;
		return this;
	}
}
