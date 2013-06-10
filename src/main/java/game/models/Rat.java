package game.models;

import game.Context;
import game.Renderable;
import game.base.Face;
import game.base.SimObject;
import game.math.Vector;
import game.nwn.Mesh;
import game.nwn.readers.Header;

import java.util.List;
import java.util.Set;

import org.lwjgl.opengl.GL11;

public class Rat implements Renderable, SimObject {
    
  Context context;
  Mesh mesh;
  double scale;
  long tick = 0;
  Vector pos = Vector.ZERO;
  Vector velocity = Vector.NORMAL;
  double mass = 0;

  private String animName;
  private Header mdl;
  private String modelName;

  public Rat(Context context, String modelName, double scale) {
    this.context = context;
    this.modelName = modelName;
    this.mdl = context.getKeyReader().getMdlReader(modelName).getHeader();
    this.mesh = new Mesh(context, mdl, 1);
    this.scale = scale;
    this.animName = "crun";
  }

  @Override
  public void render() {
    float alpha = (float)((tick % 1000.0) / 1000.0);
    pos = new Vector(0f, tick / 10f, 0f, 1.0f);
    Vector up = Vector.UP;
    double theta = velocity.theta(Vector.NORMAL, Vector.LEFT) * 360 / (2*Math.PI);
    GL11.glPushMatrix();
    GL11.glTranslated(pos.x(), pos.y(), pos.z());
    GL11.glRotated(theta, up.x(), up.y(), up.z());
    // alpha = (float)((frameIndex % 1000.0) / 1000.0);
    List<Face> faces = mesh.getFaces(animName, alpha); //(tick % 100)/100.0f);
    for(Face face: faces) {
      if ( face.getTexture() != null ) {
        face.getTexture().bind();
      }
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
        if ( tps != null && tps[i] != null ) {
          GL11.glTexCoord2d(tps[i].x(), tps[i].y());
        }
        GL11.glVertex3d(vs[i].x()*scale, vs[i].y()*scale, vs[i].z()*scale);
      }
      GL11.glEnd();
    }
    GL11.glPopMatrix();
  }

  @Override
  public void tick() {
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
