package game.gl;

public abstract class GLResource {
  
  protected Disposer disposer;

  public GLResource(Disposer disposer) {
    this.disposer = disposer;
  }
  
  protected abstract Runnable dispose();

  public void finalize() {
    disposer.dispose(dispose());
  }
}
