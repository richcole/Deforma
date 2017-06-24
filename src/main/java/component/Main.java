package component;

import component.gl.DefaultRenderTarget;
import component.gl.GlEngine;
import game.math.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {
	static private final Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(String[] args) {

		log.info("Starting");
		GlEngine glEngine = new GlEngine();
		InputController inputController = new InputController();

		Scene scene = new Scene(glEngine, inputController);
		ComponentBuilder componentBuilder = new ComponentBuilder(glEngine);

		DefaultRenderTarget renderTarget = new DefaultRenderTarget();
		DefaultCameraComponent cameraComponent = new DefaultCameraComponent(renderTarget);

		PlayerComponent playerComponent = new PlayerComponent();
		playerComponent.setInvLocalTransform(Transform.lookAt(Vector.Z, Vector.ONES.times(10000), Vector.UP));

		scene.addComponent(componentBuilder
			.newSquare(Vector.RIGHT, Vector.FWD)
			.withPosition(Vector.UP.minus().times(5))
			.withScale(100, 100, 100)
			.build());

		scene.addComponent(componentBuilder
			.newCube()
			.withPosition(Vector.UP.times(30).plus(Vector.FWD.times(30)))
			.withScale(10, 10, 10)
			.build());

		BufferedCamera bufferedCamera = new BufferedCamera(glEngine);

		scene.addComponent(componentBuilder
			.withTexture(bufferedCamera.getTexture())
			.newSquare(Vector.RIGHT.minus(), Vector.UP)
			.withPosition(Vector.ONES.times(10))
			.withScale(10, 10, 10)
			.build());

		scene.addComponent(renderTarget);
		scene.addComponent(playerComponent);

		playerComponent.addComponent(bufferedCamera);
		playerComponent.addComponent(cameraComponent);

		scene.run();
	}
}
