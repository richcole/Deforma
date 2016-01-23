package game.events;

import game.Context;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RunnableEvent extends AbstractEvent {

  private static final Logger log = LoggerFactory.getLogger(RunnableEvent.class);
  
  public Runnable action;

  public RunnableEvent(Object object, Runnable action) {
    super(object);
    this.action = action;
  }
  
  public void run() {
    try {
      this.action.run();
    }
    catch(Exception e) {
      log.error("Exception raised in runnable event", e);
    }
  }

}
