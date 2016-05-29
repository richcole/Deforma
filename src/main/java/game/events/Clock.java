package game.events;

import java.util.function.Consumer;

public class Clock {
  
  private EventBus eventBus;
  double t = System.nanoTime() * 1e-9;

  public Clock(EventBus eventBus) {
    this.eventBus = eventBus;
  }

  public void onTick(Consumer<TickEvent> action) {
    eventBus.onEvent(this, TickEvent.class, action);
  }

  public void tick() {
    double nt = System.nanoTime() * 1e-9;
    double dt = nt - t;
    t = nt;
    eventBus.emit(this, new TickEvent(dt));
  }

}
