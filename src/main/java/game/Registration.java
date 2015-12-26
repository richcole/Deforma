package game;

import game.events.Event;
import game.events.EventBus;

import java.util.function.Consumer;

public class Registration<T extends Event> {
	
	private Class<T> eventType;
	private Consumer<T> action;
	private EventBus bus;
	private Object object;
	
	public Registration(EventBus bus, Object object, Class<T> eventType, Consumer<T> action) {
		super();
		this.bus = bus;
		this.object = object;
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
	
	public void unregister() {
		bus.unregister(this);
	}

	public Object getObject() {
		return object;
	}


}
