package game;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class EventBus {
		
	private static final List<Registration<? extends Event>> EMPTY_LIST = Lists.newArrayList();

	private Map<Object, Map<Class<? extends Event>, List<Registration<? extends Event>>>> regMap = Maps.newHashMap();
	private List<Event> events = Lists.newArrayList();
	
	private boolean isClosed = false;

	public <T extends Event, S extends Event> void post(T event) {
		synchronized(this) {
			events.add(event);
		}
	}
	
	private List<Registration<? extends Event>> getRegistrations(Object object, Class<? extends Event> eventType) {
		Map<Class<? extends Event>, List<Registration<? extends Event>>> eventMap = regMap.get(object);
		if ( eventMap == null ) {
			return EMPTY_LIST;
		}
		List<Registration<? extends Event>> regList = eventMap.get(eventType);
		if ( regList == null ) {
			return EMPTY_LIST;
		}
		return regList;
	}

	private List<Registration<? extends Event>> getOrCreateRegistrations(Object object, Class<? extends Event> eventType) {
		Map<Class<? extends Event>, List<Registration<? extends Event>>> eventMap = regMap.get(object);
		if ( eventMap == null ) {
			eventMap = Maps.newHashMap();
			regMap.put(object, eventMap);
		}
		List<Registration<? extends Event>> regList = eventMap.get(eventType);
		if ( regList == null ) {
			regList = Lists.newArrayList();
			eventMap.put(eventType, regList);
		}
		return regList;
	}

	public <T extends Event> Registration<T> onEventType(Object object, Consumer<T> action, Class<T> eventType) {
		Registration<T> reg = new Registration<T>(eventType, action);
		getOrCreateRegistrations(object, eventType).add(reg);
		return reg;
	}
	
	public <T extends Event> void unregister(Object object, Registration<T> reg) {
		getRegistrations(object, reg.getEventType()).remove(reg);
	}

	public boolean isClosed() {
		return isClosed;
	}

	public void processEvents() {
		if ( events.isEmpty() ) {
			return;
		}
		List<Event> currentEvents;
		synchronized(this) {
			currentEvents = events;
			events = Lists.newArrayList();
		}
		for(Event event: currentEvents) {
			for(Registration<? extends Event> reg: getRegistrations(event.getObject(), event.getClass())) {
				try {
					dispatch(reg.getAction(), event);
				}
				catch(Exception e) {
					// ignore for now
				}
			}
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends Event> void dispatch(Consumer<T> action, Event event) {
		action.accept((T)event);
	}

}
