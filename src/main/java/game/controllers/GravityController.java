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
		Vector p = view.getPosition().minus(Vector.U2.times(2).plus(view.getForward()));
    if ( hm.contains(p.plus(Vector.U1).plus(Vector.U2)) && hm.contains(p.minus(Vector.U1).minus(Vector.U2)) ) {
      view.move(hm.getGravityMovement(p, view.getLeft(), 0.2, speed));
    }
	}
	
	public void unregister() {
		reg.unregister();
	}
	
}
