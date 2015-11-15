package game;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GLDisplay extends AbstractGLResource {

	boolean initialized = false;
	
	public void init() {		
		try {
			Display.create();
		    Display.setTitle("Game2"); 
	        Display.setResizable(true); 
	        Display.setDisplayMode(new DisplayMode(800, 800)); 
	        Display.setVSyncEnabled(true); 
	        Display.setFullscreen(false);
			GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
	

	public void dispose() {
		Display.destroy();
	}


}
