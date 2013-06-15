package game.models;

import game.Context;
import game.Renderable;
import game.enums.Model;
import game.math.Vector;

import org.lwjgl.opengl.GL11;

public class TerrainTile implements Renderable {
  
  Model model;
  Vector pos;
  Context context;
  double scale;
  AnimMeshRenderer renderer;
  AnimMesh animMesh;
  

  public TerrainTile(Context context, Vector pos, Model model) {
    this.context = context;
    this.pos = pos;
    this.scale = context.getScale();
    this.renderer = new AnimMeshRenderer(context);
    this.animMesh = context.getModels().getAnimMesh(model);
  }

  @Override
  public void render() {
    GL11.glPushMatrix();
    GL11.glTranslated(pos.x(), pos.y(), pos.z());
    GL11.glScaled(scale, scale, scale);
    renderer.render(animMesh);
    GL11.glPopMatrix();
  }

  public void register() {
    context.getScene().register(this);
  }
  
  public void setModel(String modelName) {
    this.animMesh = context.getModels().getAnimMesh(modelName);
  }

  public void setPos(Vector pos) {
    this.pos = pos;
  }

}
