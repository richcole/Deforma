package game.gl;

import game.events.Clock;
import game.events.EventBus;
import game.image.Image;

public class GLFactory {

  private Disposer disposer;
  private Clock clock;
  private EventBus eventBus;

  public GLFactory(Disposer disposer, EventBus eventBus, Clock clock) {
    this.disposer = disposer;
    this.eventBus = eventBus;
    this.clock = clock;
  }
  
  public GLVertexArray newVertexArray() {
    return new GLVertexArray(disposer);
  }

  public GLBuffer newBuffer() {
    return new GLBuffer(disposer);
  }

  public GLTexture newTexture(Image image) {
    return new GLTexture(disposer, image);
  }

  public GLProgram newProgram() {
    return new GLProgram(disposer);
  }

  public GLShader newShader(int glVertexShader) {
    return new GLShader(disposer, glVertexShader);
  }

  public GLDisplay newDisplay() {
    return new GLDisplay(disposer, eventBus, clock);
  }
}
