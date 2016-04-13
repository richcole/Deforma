package game.model;

import game.gl.GLFactory;
import game.gl.GLTexture;

public class CompiledTexture {

  private GLTexture tex;
  private CompositeImage compositeImage;

  public CompiledTexture(GLFactory glFactory, CompositeImage compositeImage) {
    this.tex = glFactory.newTexture(compositeImage);
    this.compositeImage = compositeImage;
  }

  public GLTexture getTex() {
    return tex;
  }

  public CompositeImage getCompositeImage() {
    return compositeImage;
  }
  
}
