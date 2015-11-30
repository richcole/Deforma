package game;

import java.util.function.Consumer;

import org.lwjgl.opengl.Display;

import game.events.ApplicationCloseEvent;
import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;

public class CloseWatcher implements Consumer<TickEvent> {

	private boolean isClosed = false;
	private EventBus eventBus;
  private Clock clock;
  private Object object;
	
	public CloseWatcher(Clock clock, Object object, EventBus eventBus) {
		this.eventBus = eventBus;
    this.isClosed = false;
    this.clock = clock;
    this.object = object;
    eventBus.onEventType(clock, this, TickEvent.class);
	}
	
	public boolean isClosed() {
		return isClosed;
	}

  @Override
  public void accept(TickEvent t) {
    if ( ! isClosed && Display.isCloseRequested() ) {
      eventBus.post(new ApplicationCloseEvent(object));
    }
    isClosed = Display.isCloseRequested();
  }
}
