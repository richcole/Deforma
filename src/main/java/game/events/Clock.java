package game.events;

public class Clock {

  private EventBus eventBus;
  private long prevTick;
  private double tickTime = 0.01;

  public Clock(EventBus eventBus) {
    this.eventBus = eventBus;
    this.prevTick = System.nanoTime();
  }

  public void run() {
    long currTick = System.nanoTime();
    double dt = ( currTick - prevTick ) / 1e9;
    if ( dt > tickTime ) {
      eventBus.post(new TickEvent(this, dt));
      prevTick = currTick;
    }
    
  }

}
