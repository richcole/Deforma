package game;

import game.events.EventBus;
import game.events.RunnableEvent;

public class EventRunner {

  public EventRunner(EventBus eventBus) {
    eventBus.onEventType(eventBus, (RunnableEvent e) -> e.run(), RunnableEvent.class);
  }
}
