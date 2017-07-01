package component;

import component.gl.DefaultRenderTarget;
import component.gl.GlEngine;
import game.math.Matrix;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class DefaultCameraComponent extends DefaultComponent implements CameraComponent {

	private RenderTarget renderTarget;

	@Override
	public void render(Scene scene, GlEngine glEngine) {
		updateTransform(scene);
		renderTarget.bind(glEngine);
		scene.getRootComponent().forEachDecendent(RenderComponent.class, (renderComponent) -> renderComponent.render(scene, this));
	}

	private void updateTransform(Scene scene) {
		double dydx = scene.getGlEngine().getHeightToWidthRatio();
		double dx = 0.3;
		double dy = dydx * dx;
		double dz = 0.3;
		setLocalTransform(new DefaultTransform(Matrix.frustum(-dx, dx, dy, -dy, dz, 10000)).invert());
	}
}
