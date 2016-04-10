package game.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.Context;
import game.Registration;
import game.Stats;
import game.View;
import game.basicgeom.Vector;
import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.geom.HeightMap;

public class GravityController {
	
	private static final Logger log = LoggerFactory.getLogger(GravityController.class);

	private View view;
	private HeightMap hm;
	private double speed = 10;
	private Registration<TickEvent> reg;

  private Stats stats;

	public GravityController(EventBus eventBus, Clock clock, View view, HeightMap hm, Stats stats) {
		this.view = view;
		this.hm = hm;
		this.reg = eventBus.onEventType(clock, (TickEvent e) -> onTick(e), TickEvent.class);
		this.stats = stats;
	}

	private void onTick(TickEvent e) {
		Vector p = view.getPosition().minus(Vector.U2.times(4));
		stats.viewP = view.getPosition();
		stats.gravityP = p;
		stats.insideField = false;
    if ( hm.contains(p.plus(Vector.U1).plus(Vector.U2)) && hm.contains(p.minus(Vector.U1).minus(Vector.U2)) ) {
      stats.insideField = true;
      view.move(hm.getGravityMovement(p, view.getLeft(), view.getForward(), speed));
    }
	}
	
	public void unregister() {
		reg.unregister();
	}
	
}
