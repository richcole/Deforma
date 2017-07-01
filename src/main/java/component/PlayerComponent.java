package component;

import game.math.Matrix;
import game.math.Vector;
import lombok.Getter;
import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerComponent extends DefaultComponent {
	private static final Logger log = LoggerFactory.getLogger(PlayerComponent.class);

	@Getter
	PhysicalObject physicalObject = new PhysicalObject();

	public PlayerComponent() {
	}

	@Override
	public Transform getLocalTransform() {
		return physicalObject.getLocalTransform();
	}
}
