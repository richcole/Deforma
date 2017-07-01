package component;

import component.gl.GlEngine;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class Scene {

	private static Logger log = LoggerFactory.getLogger(Scene.class);

	@Getter
	private final GlEngine glEngine;

	@Getter
	private final InputController inputController;

	@Getter
	private final Component rootComponent = new DefaultComponent();

	private boolean shouldTerminate = false;
	private long currTime = System.currentTimeMillis();
	private long lastTime = currTime;

	public void run() {
		rootComponent.forEachDecendent(Component.class, (c) -> c.init(this));

		while(! shouldTerminate && ! glEngine.checkForClosed()) {
			inputController.processEvents();

			lastTime = currTime;
			currTime = System.currentTimeMillis();

			rootComponent.forEachDecendent(Component.class, (c) -> c.update(this));

			rootComponent.forEachDecendent(RenderTarget.class, (renderTarget) -> {
				renderTarget.bind(glEngine);
				renderTarget.preRender(glEngine);
			});

			rootComponent.forEachDecendent(CameraComponent.class, (cameraComponent) -> {
				cameraComponent.render(this, glEngine);
			});

			rootComponent.forEachDecendent(RenderTarget.class, (renderTarget) -> {
				renderTarget.postRender(glEngine);
			});
		}
	}

	public void addComponent(Component childComponent) {
		rootComponent.addComponent(childComponent);
	}

	public double getDeltaTime() {
		return (double)(currTime - lastTime) / 1000;
	}
}
