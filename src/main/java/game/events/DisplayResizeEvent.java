package game.events;

public class DisplayResizeEvent extends AbstractEvent {

  public double dx, dy;

  public DisplayResizeEvent(Object object, double dx, double dy) {
    super(object);
    this.dx = dx;
    this.dy = dy;
  }

}
