package game.controllers;

import game.Registration;
import game.Stats;
import game.View;
import game.basicgeom.Vector;
import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.geom.HeightMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
    Vector left = view.getLeft();
    Vector fwd = view.getForward();
    if ( hm.contains(p.plus(left).plus(fwd)) && hm.contains(p.minus(left).minus(fwd)) ) {
      stats.insideField = true;
      view.move(hm.getGravityMovement(p, left, fwd, speed));
    }
	}
	
	public void unregister() {
		reg.unregister();
	}
	
}
