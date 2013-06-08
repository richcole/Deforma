package game.models;

import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

import game.Context;
import game.Renderable;
import game.base.Face;
import game.base.SimObject;
import game.base.Texture;
import game.math.Vector;
import game.nwn.Mesh;
import game.nwn.readers.Header;

public class Rat implements Renderable, SimObject {
  
  private static Logger logger = Logger.getLogger(Rat.class);
  
  Context context;
  Mesh mesh;
  double scale;
  long tick = 0;
  Vector pos = Vector.ZERO;
  double mass = 0;
  long frameIndex = 800;

  Rotation rotation;
  Rect     basis;

  private String animName;
  private Header mdl;
  private String mdlName;
  private float radius;

  public Rat(Context context, double scale) {
    this.context = context;
    this.mdlName = "c_wererat";
    this.mdl = context.getKeyReader().getMdlReader(mdlName).getHeader();
    this.mesh = new Mesh(context, mdl, 1);
    this.scale = scale;
    this.rotation = new Rotation(-90, new Vector(1f, 0f, 0f, 1f));
    this.radius = mdl.getModel().getRadius();
    this.basis = new Rect(pos, Vector.U1.scaleTo(radius), Vector.U2.scaleTo(radius));
    this.frameIndex = 0;
    this.animName = "cwalk";
  }

  @Override
  public void render() {
    GL11.glPushMatrix();
    basis.render();
    float alpha = (float)((tick % 1000.0) / 1000.0);
    pos = new Vector(0f, tick / 10f, 0f, 1.0f);
    // alpha = (float)((frameIndex % 1000.0) / 1000.0);
    List<Face> faces = mesh.getFaces(animName, alpha); //(tick % 100)/100.0f);
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
        GL11.glVertex3d(vs[i].x()*scale+pos.x(), vs[i].y()*scale+pos.y(), vs[i].z()*scale+pos.z());
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
  public Vector pos() {
    return pos;
  }

  @Override
  public double mass() {
    return mass;
  }

  public void nextFrame(boolean pressed) {
    if ( pressed ) {
      frameIndex += 10;
    }
  }

  public long getFrameIndex() {
    return frameIndex;
  }
  
  public Set<Integer> getNumberOfFrames() {
    return mesh.getNumberOfFrames(animName);
  }
}
