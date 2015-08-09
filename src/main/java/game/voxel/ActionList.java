package game.voxel;

import java.util.LinkedHashMap;

import com.google.common.collect.Maps;

public class ActionList implements Action {
	
	LinkedHashMap<String, Action> runables = Maps.newLinkedHashMap();
	private Context context;
	
	public ActionList(Context context) {
		super();
		this.context = context;
	}

	public void init() {
		for(Action runable: runables.values()) {
			runable.init();
		}
	}
	
	public void dispose() {
		for(Action runable: runables.values()) {
			runable.dispose();
		}
	}

	public void run() {
		while( ! context.getCloseWatcher().isClosed() ) {
			for(Action runable: runables.values()) {
				runable.run();
			}
		}
	}
	
	void add(String name, Action renderer) {
		runables.put(name, renderer);
	}


}
