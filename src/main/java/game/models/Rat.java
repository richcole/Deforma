package game.models;

import java.util.List;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import game.Context;
import game.Renderable;
import game.base.Face;
import game.base.SimObject;
import game.base.Texture;
import game.math.Vector;
import game.nwn.Mesh;

public class Rat implements Renderable, SimObject {
  
  private static Logger logger = Logger.getLogger(Rat.class);
  
  Context context;
  Mesh mesh;
  double scale;
  long tick = 0;
  Vector pos = Vector.ZERO;
  double mass = 0;

  Rotation rotation;

  public Rat(Context context, double scale, Rotation rotation) {
    this.context = context;
    this.mesh = new Mesh(context, context.getKeyReader().getMdlReader("c_wererat").getHeader(), 1);
    this.scale = scale;
    this.rotation = rotation;
  }

  @Override
  public void render() {
    rotation.render();
    int animIndex = (int) tick / 100;
    List<Face> faces = mesh.getFaces(animIndex, animIndex + 1, (tick % 100) / 100f);
    for(Face face: faces) {
      face.getTexture().bind();
      GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, context.getColors().getColor(face.getDiffuse()));
      GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, context.getColors().getColor(face.getSpecular()));
      GL11.glBegin(GL11.GL_TRIANGLES);
      Vector normal = face.getNormal();
      Vector[] colors = face.getColors();
      
      Vector[] vs = face.getVertices();
      Vector[] tps = face.getTexturePoints();
      for(int i=0;i<vs.length; ++i) {
        GL11.glNormal3d(normal.x(), normal.y(), normal.z());
        GL11.glColor3d(colors[i].x(), colors[i].y(), colors[i].z());
        GL11.glTexCoord2d(tps[i].x(), tps[i].y());
        GL11.glVertex3d(vs[i].x()*scale, vs[i].y()*scale, vs[i].z()*scale);
      }
      GL11.glEnd();
    }
  }

  @Override
  public void tick() {
    tick = tick + 1;
  }

  @Override
  public Vector pos() {
    return pos;
  }

  @Override
  public double mass() {
    return mass;
  }
}
