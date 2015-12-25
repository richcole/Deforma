package game.events;

public class KeyUpEvent extends AbstractEvent {

  public int key;

  public KeyUpEvent(Object object, int key) {
    super(object);
    this.key = key;
  }

}
