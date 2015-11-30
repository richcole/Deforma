package game;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

public class Simulator implements Consumer<TickEvent> {
	
	long lastTick;
	long tickTime = 10;
	
	EventBus eventBus;
	
	Simulator(Clock clock, EventBus eventBus) {
    lastTick = System.currentTimeMillis();
    this.eventBus = eventBus; 
	  eventBus.onEventType(clock, this, TickEvent.class);
	}
	
	public void accept(TickEvent tickEvent) {
		long tick = System.currentTimeMillis();
		long dt = tick - lastTick;
		if (dt > tickTime) {
			lastTick = tick;
			eventBus.post(new TickEvent(this, dt));
		}
	}
}
