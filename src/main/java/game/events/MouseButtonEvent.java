package game.events;

public class MouseButtonEvent extends AbstractEvent {
  public int button;
  public boolean isDown;
  
  public MouseButtonEvent(Object object, int button, boolean isDown) {
    super(object);
    this.button = button;
    this.isDown = isDown;
  }

}
