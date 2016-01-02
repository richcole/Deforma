package game.events;

public class KeyDownEvent extends AbstractEvent {

  public int key;

  public KeyDownEvent(Object object, int key) {
    super(object);
    this.key = key;
  }

  public int getKey() {
    return key;
  }

}
