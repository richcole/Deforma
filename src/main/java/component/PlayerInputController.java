package component;

import game.math.Matrix;
import org.lwjgl.input.Keyboard;

public class PlayerInputController extends DefaultComponent {

	private PhysicalObject physicalObject;

	public PlayerInputController(PhysicalObject physicalObject) {
		this.physicalObject = physicalObject;
	}

	@Override
	public void update(Scene scene) {
		double dt = scene.getDeltaTime();
		InputController ic = scene.getInputController();
		boolean updated = false;
		if (ic.isKeyDown(Keyboard.KEY_W)) {
			physicalObject.moveForward(dt);
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_A)) {
			physicalObject.moveLeft(dt);
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_S)) {
			physicalObject.moveBackward(dt);
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_D)) {
			physicalObject.moveRight(dt);
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_Q)) {
			physicalObject.moveUp(dt);
			updated = true;
		}
		if (ic.isKeyDown(Keyboard.KEY_E)) {
			physicalObject.moveDown(dt);
			updated = true;
		}
		if (ic.isMouseDown(0)) {
			double drx = ic.getMdx();
			double dry = ic.getMdy();
			physicalObject.updateRotation(drx, dry);
			updated = true;
		}
		if (updated) {
			physicalObject.updateTransform();
		}
	}

}
