package component;

import component.gl.GlEngine;
import game.gl.GLTexture;

public class BufferedCamera extends DefaultComponent {

	private DefaultCameraComponent camera;
	private FboRenderTarget renderTarget;

	public BufferedCamera(GlEngine glEngine) {
		this.renderTarget = new FboRenderTarget(glEngine);
		this.camera = new DefaultCameraComponent(renderTarget);

		addComponent(renderTarget);
		addComponent(camera);
	}

	public GLTexture getTexture() {
		return renderTarget.getTexture();
	}
}
