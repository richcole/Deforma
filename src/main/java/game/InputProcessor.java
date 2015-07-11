package game;

import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;

public class InputProcessor implements Action {
	
	List<InputController> controllers = Lists.newArrayList();
	
	int mx, my, pmx, pmy;

	public void init() {
		pmx = Mouse.getX();
		pmy = Mouse.getY();
	}

	public void run() {
		mx = Mouse.getX();
		my = Mouse.getY();
		
		for(InputController controller: controllers) {
			controller.mouseMove(mx - pmx, my - pmy);
		}
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				for(InputController controller: controllers) {
						controller.keyDown(Keyboard.getEventKey());
				}
			}
			else {
				for(InputController controller: controllers) {
					controller.keyUp(Keyboard.getEventKey());
				}
			}
		}
		
	}

	public void dispose() {
	}

	public void add(InputController inputController) {
		controllers.add(inputController);
	}

}
