package game.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.Context;
import game.HeightMap;
import game.Registration;
import game.Vector;
import game.View;
import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;

public class GravityController {
	
	private static final Logger log = LoggerFactory.getLogger(GravityController.class);

	private View view;
	private HeightMap hm;
	private double speed = 10;
	private Registration<TickEvent> reg;

	public GravityController(EventBus eventBus, Clock clock, View view, HeightMap hm) {
		this.view = view;
		this.hm = hm;
		this.reg = eventBus.onEventType(clock, (TickEvent e) -> onTick(e), TickEvent.class);
	}

	private void onTick(TickEvent e) {
		Vector p = view.getPosition().plus(Vector.U2.times(2));
		if ( hm.contains(p) ) {
			Vector m = hm.getGravityMovement(p, speed);
			// log.info("pos " + p + " movement " + m);
			view.move(m);
		}
	}
	
	public void unregister() {
		reg.unregister();
	}
	
}
