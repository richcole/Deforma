package game;

import java.util.LinkedHashMap;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class ActionList implements Action {
	
	List<Action> runables = Lists.newArrayList();
	EventBus eventBus;
	
	public ActionList(EventBus eventBus) {
		super();
		this.eventBus = eventBus;
	}

	public void init() {
		for(Action runable: runables) {
			runable.init();
		}
	}
	
	public void dispose() {
		for(Action runable: runables) {
			runable.dispose();
		}
	}

	public void run() {
		while( ! eventBus.isClosed() ) {
			eventBus.processEvents();
			for(Action runable: runables) {
				runable.run();
			}
		}
	}
	
	void add(Action renderer) {
		runables.add(renderer);
	}


}
