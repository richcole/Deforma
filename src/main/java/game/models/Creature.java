package game.models;

import game.Context;
import game.Renderable;
import game.base.Face;
import game.base.SimObject;
import game.base.Texture;
import game.base.io.Serializer;
import game.math.Vector;
import game.nwn.NwnMesh;
import game.nwn.NwnTextureProvider;
import game.nwn.readers.MdlModel;

import java.io.File;
import java.util.List;

import org.lwjgl.opengl.GL11;

public class Creature implements Renderable, SimObject {
    
  Context context;
  NwnMesh mesh;
  double scale;
  long tick = 0;
  Vector pos = Vector.ZERO;
  Vector velocity = Vector.NORMAL;
  double mass = 0;
  double alpha = 0;
  
  Vector dest;

  private String animName;
  private AnimMesh animMesh;
  private String modelName;

  public Creature(Context context, String modelName, String animName, double scale) {
    this.context = context;
    this.modelName = modelName;
    Serializer serializer = new Serializer();
    this.animMesh = serializer.deserialize(new File("res/wererat.mdl.gz"), AnimMesh.class);
    this.scale = scale;
    this.animName = animName;
  }

  @Override
  public void render() {
    AnimMeshRenderer renderer = new AnimMeshRenderer(context);
    GL11.glPushMatrix();
    GL11.glTranslated(pos.x(), pos.y(), pos.z());
    GL11.glScaled(scale, scale, scale);
    renderer.render(animMesh, animName, alpha);
    GL11.glPopMatrix();
  }
  
  @Override
  public void tick() {
    alpha = (float)((tick % 1000.0) / 1000.0);
    pos = new Vector(0f, tick * scale / 500f, 0f, 1.0f);
    tick = tick + 1;
  }

  @Override
  public Vector getPos() {
    return pos;
  }

  @Override
  public double getMass() {
    return mass;
  }
}
