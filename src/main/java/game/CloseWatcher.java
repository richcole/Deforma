package game;

import org.lwjgl.opengl.Display;

import game.events.ApplicationCloseEvent;

public class CloseWatcher implements Action {

	private boolean isClosed = false;
	private EventBus eventBus;
	private Object object;
	
	public CloseWatcher(Object object, EventBus eventBus) {
		this.eventBus = eventBus;
		this.object = object;
	}
	
	public void init() {
		isClosed = false;
	}

	public void run() {
		if ( ! isClosed && Display.isCloseRequested() ) {
			eventBus.post(new ApplicationCloseEvent(object));
		}
		isClosed = Display.isCloseRequested();
	}
	
	public void dispose() {
	}

	public boolean isClosed() {
		return isClosed;
	}
}
