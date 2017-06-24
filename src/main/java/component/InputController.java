package component;

import lombok.Getter;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.BitSet;

public class InputController {
	private static final Logger log = LoggerFactory.getLogger(InputController.class);

	public static final int NUM_BUTTONS = 8;
	public static final int NUM_KEYS = 256;

	private BitSet keysDown = new BitSet(NUM_KEYS);
	private BitSet mouseButtonsDown = new BitSet(NUM_BUTTONS);

	@Getter
	private double mdx = 0;

	@Getter
	private double mdy = 0;

	public void processEvents() {
		mdx = 0;
		mdy = 0;
		while(Keyboard.next()) {
			int key = Keyboard.getEventKey();
			if (key >= 0 && key < 256) {
				boolean eventKeyState = Keyboard.getEventKeyState();
				keysDown.set(key, eventKeyState);
			}
		}
		while(Mouse.next()) {
			if (Mouse.getEventButton() != -1) {
				mouseButtonsDown.set(Mouse.getEventButton(), Mouse.getEventButtonState());
			}
			mdx += Mouse.getDX();
			mdy += Mouse.getDY();
		}
	}

	public boolean isKeyDown(int key) {
		if (key >= 0 && key < NUM_KEYS) {
			return keysDown.get(key);
		}
		return false;
	}

	public boolean isMouseDown(int buttonIndex) {
		if (buttonIndex >= 0 && buttonIndex < NUM_BUTTONS) {
			return mouseButtonsDown.get(buttonIndex);
		}
		return false;
	}

}
