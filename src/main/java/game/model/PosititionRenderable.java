package game.model;

import game.math.Matrix;
import game.math.Vector;

public class PosititionRenderable implements Renderable {
  
  private Matrix modelTr;
  private CompiledMesh mesh;

  public PosititionRenderable(CompiledMesh mesh, Vector position) {
    this.modelTr = Matrix.translate(position);
    this.mesh = mesh;
  }

  @Override
  public void render() {
    mesh.render(modelTr);
  }

}
