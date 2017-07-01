package component;

import org.lwjgl.input.Keyboard;

public class UIComponentController extends DefaultComponent {

	private Component component;

	public UIComponentController(UIComponent component) {
		this.component = component;
	}

	@Override
	public void update(Scene scene) {
		InputController ic = scene.getInputController();
		if (ic.isKeyPressed(Keyboard.KEY_F1)) {
			component.setEnabled(! component.isEnabled());
		}
	}

}
