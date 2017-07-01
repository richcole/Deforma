package component;

import game.model.CompiledMesh;

public class MeshComponent extends DefaultComponent implements RenderComponent {

	CompiledMesh compiledMesh;

	public MeshComponent(Transform transform, CompiledMesh compiledMesh) {
		super(transform);
		this.compiledMesh = compiledMesh;
	}

	@Override
	public void render(Scene scene, CameraComponent camera) {
		compiledMesh.render(camera.getGlobalTransform().getInvTr(), getGlobalTransform().getTr());
	}
}
