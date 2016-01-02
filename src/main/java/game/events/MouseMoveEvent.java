package game.events;

public class MouseMoveEvent extends AbstractEvent {

  public int dx;
  public int dy;

  public MouseMoveEvent(Object object, int dx, int dy) {
    super(object);
    this.dx = dx;
    this.dy = dy;
  }

}
