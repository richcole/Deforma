package game.events;

public class TickEvent extends AbstractEvent {

  public double dt;

  public TickEvent(Object object, double dt) {
    super(object);
    this.dt = dt;
  }

}
