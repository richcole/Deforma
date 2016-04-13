package game.events;

public class TickEvent implements Event {

  public double dt;
  
  public TickEvent(double dt) {
    super();
    this.dt = dt;
  }
}
