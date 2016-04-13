package game.gl;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public class GLDisplay extends GLResource {

  private boolean initialized = false;
  private boolean isClosed = false;
  private EventBus eventBus;

  public GLDisplay(Disposer disposer, EventBus eventBus, Clock clock) {
    super(disposer);
    
    this.eventBus = eventBus;
    
    try {
      Display.create();
      Display.setTitle("Game2");
      Display.setResizable(true);
      Display.setDisplayMode(new DisplayMode(800, 800));
      Display.setVSyncEnabled(true);
      Display.setFullscreen(false);
      GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
      
      eventBus.onEvent(clock, TickEvent.class, (tickEvent) -> onTick(tickEvent));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
  
  public void onTick(TickEvent event) {
    if (Display.wasResized()) {
      GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
      eventBus.emit(this, new DisplayResizeEvent(Display.getWidth(), Display.getHeight()));
    }
    if ( ! isClosed && Display.isCloseRequested() ) {
      isClosed = true;
      eventBus.emit(this, new DisplayCloseEvent());
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
