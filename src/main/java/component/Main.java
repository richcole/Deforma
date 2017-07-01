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
		SimpleClock clock = new SimpleSystemClock();
		GlEngine glEngine = new GlEngine();
		InputController inputController = new InputController();

		Scene scene = new Scene(glEngine, inputController);
		ComponentBuilder componentBuilder = new ComponentBuilder(glEngine);
		UIComponent uiComponent = new UIComponent(glEngine, clock);

		DefaultRenderTarget defaultRenderTarget = new DefaultRenderTarget();
		DefaultCameraComponent defaultCameraComponent = new DefaultCameraComponent(defaultRenderTarget);

		PlayerComponent playerComponent = new PlayerComponent();
		playerComponent.setLocalTransform(Transform.lookAt(Vector.Z, Vector.ONES.times(10000), Vector.UP).invert());

		BufferedCamera bufferedCamera = new BufferedCamera(glEngine);

		RenderComponent square = componentBuilder
			.newSquare(Vector.RIGHT, Vector.FWD)
			.withPosition(Vector.UP.minus().times(5))
			.withScale(100, 100, 100)
			.build();

		RenderComponent cube = componentBuilder
			.newCube()
			.withPosition(Vector.UP.times(30).plus(Vector.FWD.times(30)))
			.withScale(10, 10, 10)
			.build();

		RenderComponent cameraSquare = componentBuilder
			.withTexture(bufferedCamera.getTexture())
			.newSquare(Vector.RIGHT, Vector.UP)
			.withPosition(new Vector(10, 10, -10))
			.withScale(10, 10, 10)
			.build();

		PlayerInputController playerInputController = new PlayerInputController(playerComponent.getPhysicalObject());
		UIComponentController uiInputController = new UIComponentController(uiComponent);
		PositionPainter printPositionController = new PositionPainter(uiComponent, playerComponent.getPhysicalObject());

		scene.addComponent(playerInputController);
		scene.addComponent(uiInputController);
		uiComponent.addPainter(printPositionController);

		scene.addComponent(square);
		scene.addComponent(cube);
		scene.addComponent(cameraSquare);

		scene.addComponent(defaultRenderTarget);
		scene.addComponent(playerComponent);

		playerComponent.addComponent(bufferedCamera);
		playerComponent.addComponent(defaultCameraComponent);

		scene.addComponent(uiComponent);

		scene.run();
	}
}
