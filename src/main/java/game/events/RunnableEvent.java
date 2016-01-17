package game.events;

public class RunnableEvent extends AbstractEvent {

  public Runnable action;

  public RunnableEvent(Object object, Runnable action) {
    super(object);
    this.action = action;
  }
  
  public void run() {
    this.action.run();
  }

}
