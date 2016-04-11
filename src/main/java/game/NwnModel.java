package game;

import game.models.AnimMesh;
import game.models.AnimMeshRenderer;

public class NwnModel implements Renderable {

  private AnimMeshRenderer animRenderer;
  private AnimMesh mesh;

  public NwnModel(AnimMeshRenderer animRenderer, AnimMesh mesh) {
    this.animRenderer = animRenderer;
    this.mesh = mesh;
  }

  @Override
  public void render() {
    animRenderer.render(mesh);
  }

}
