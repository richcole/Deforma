package game;

import java.util.LinkedHashMap;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

import com.google.common.collect.Maps;

public class Renderer implements Action {

	LinkedHashMap<String, Renderable> renderables = Maps.newLinkedHashMap();

	public Renderer() {
	}
	
	public void init() {
	}
	
	public void add(String name, Renderable r) {
		renderables.put(name, r);
	}
	
	public void dispose() {
		Display.destroy();
	}

	public void run() {
		GL11.glClearColor(0, 0, 0, 1); // black
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		for(Renderable r: renderables.values()) {
			r.render();
		}
		Display.update();
        Display.sync(60);
	}

}
