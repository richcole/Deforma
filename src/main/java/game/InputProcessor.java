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
		
		while(Mouse.next()) {
			int button = Mouse.getEventButton();
			if ( button != -1 ) {
				boolean state = Mouse.getEventButtonState();
				for(InputController controller: controllers) {
					if ( state ) {
						controller.mouseDown(button);
					} 
					else {
						controller.mouseUp(button);
					}
				}
			}
		}
		
		for(InputController controller: controllers) {
			controller.mouseMove(mx - pmx, my - pmy);
			pmx = mx;
			pmy = my;
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
