package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayResizer  implements Action {
	
	final static Logger log = LoggerFactory.getLogger(DisplayResizer.class);
	
	DisplayResizer() {
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void run() {
		if (Display.wasResized()) {
	        resize();
		}
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	private void resize() {
		log.info("Resize display " + Display.getWidth() + " " + Display.getHeight());
		GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
	}

}
