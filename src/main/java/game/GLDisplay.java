package game;

import game.events.Clock;
import game.events.DisplayCloseEvent;
import game.events.DisplayResizeEvent;
import game.events.EventBus;
import game.events.TickEvent;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GLDisplay extends GLResource {

  boolean initialized = false;
  boolean isClosed = false;

  public GLDisplay(Clock clock, EventBus eventBus) {
    super(eventBus);
    try {
      Display.create();
      Display.setTitle("Game2");
      Display.setResizable(true);
      Display.setDisplayMode(new DisplayMode(800, 800));
      Display.setVSyncEnabled(true);
      Display.setFullscreen(false);
      GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
      
      eventBus.onEventType(clock, (tickEvent) -> onTick(tickEvent), TickEvent.class);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public void onTick(TickEvent event) {
    if (Display.wasResized()) {
      GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
      eventBus.post(new DisplayResizeEvent(this, Display.getWidth(), Display.getHeight()));
    }
    if ( ! isClosed && Display.isCloseRequested() ) {
      isClosed = true;
      eventBus.post(new DisplayCloseEvent(this));
    }
  }

  public boolean isClosed() {
    return isClosed;
  }

  public Runnable dispose() {
    return () -> Display.destroy();
  }

  public double getHeightToWidthRatio() {
    return ((double)Display.getHeight()) / Display.getWidth(); 
  }

}
