package game.events;

public class DisplayCloseEvent extends AbstractEvent {

  public double dx, dy;

  public DisplayCloseEvent(Object object) {
    super(object);
  }

}
