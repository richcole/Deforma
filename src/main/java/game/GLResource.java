package game;

import game.events.EventBus;
import game.events.RunnableEvent;

public abstract class GLResource {
  
  private EventBus eventBus;

  public GLResource(EventBus eventBus) {
    this.eventBus = eventBus;
  }
  
  protected abstract Runnable dispose();

  public void finalize() {
    eventBus.post(new RunnableEvent(eventBus, dispose()));
  }
}
