package game.gl;

import java.util.LinkedHashMap;

import com.google.common.collect.Maps;

public class GLResourceList implements GLResource {
	
	LinkedHashMap<String, GLResource> runables = Maps.newLinkedHashMap();
	
	public GLResourceList() {
		super();
	}

	public void init() {
		for(GLResource runable: runables.values()) {
			runable.init();
		}
	}
	
	public void dispose() {
		for(GLResource runable: runables.values()) {
			runable.dispose();
		}
	}
	
	public void add(String name, GLResource renderer) {
		runables.put(name, renderer);
	}


}
