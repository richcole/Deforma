package game;

import game.events.Event;

import java.util.function.Consumer;

public class Registration<T extends Event> {
	
	Class<T> eventType;
	Consumer<T> action;
	
	public Registration(Class<T> eventType, Consumer<T> action) {
		super();
		this.eventType = eventType;
		this.action = action;
	}

	public Class<T> getEventType() {
		return eventType;
	}

	public void setEventType(Class<T> eventType) {
		this.eventType = eventType;
	}

	public Consumer<T> getAction() {
		return action;
	}

	public void setAction(Consumer<T> action) {
		this.action = action;
	}


}
