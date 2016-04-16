package game.model;

import game.math.Matrix;
import game.math.Vector;

public class PosititionRenderable implements Renderable {
  
  private Matrix modelTr;
  private TransformRenderable mesh;

  public PosititionRenderable(TransformRenderable mesh, Matrix modelTr) {
    this.modelTr = modelTr;
    this.mesh = mesh;
  }

  @Override
  public void render(Matrix viewTr) {
    mesh.render(viewTr, modelTr);
  }

}
