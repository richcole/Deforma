package game.models;

import game.base.Face;
import game.basicgeom.Matrix;
import game.basicgeom.Quaternion;
import game.basicgeom.Vector;
import game.enums.Anim;
import game.events.EventBus;
import game.gl.GLTexture;
import game.models.AnimMesh.Animation;
import game.models.AnimMesh.AnimationNode;
import game.models.AnimMesh.Node;
import game.models.AnimMesh.PositionTiming;
import game.models.AnimMesh.RotationTiming;
import game.models.AnimMesh.Timing;
import game.nwn.readers.KeyReader;

import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnimMeshRenderer {
  
  public static Logger logger = LoggerFactory.getLogger(AnimMeshRenderer.class);
  
  private ColorFactory colorFactory;
  private TextureCache textureCache;
  private KeyReader keyReader;
  private EventBus eventBus;
  
  public AnimMeshRenderer(EventBus eventBus, KeyReader keyReader, TextureCache textureCache, ColorFactory colorFactory) {
    super();
    this.textureCache = textureCache;
    this.colorFactory = colorFactory;
    this.keyReader = keyReader;
    this.textureCache = textureCache;
  }

  public void render(AnimMesh m, String animName, double alpha) {
    Animation animation = m.getAnimations().get(animName);
    if ( animation != null ) {
      alpha = alpha * animation.getTiming();
    }
    renderNode(m.getRoot(), animName, alpha, Matrix.IDENTITY);
  }

  public void render(AnimMesh animMesh) {
    render(animMesh, Anim.NONE.getName(), 0);
  }
  
  private void renderNode(Node node, String animName, double alpha, Matrix tr) {
    tr = updateTransform(node, animName, alpha, tr, true);

    bindTexture(node);
    renderMaterial(node);

    for(Face face: node.getFaces()) {
      renderFace(node, animName, face, tr, node.getTextureName());
    }
    
    for(Node child: node.getChildren()) {
      renderNode(child, animName, alpha, tr);
    }   
  }

  public void renderMaterial(Node node) {
    renderMaterial(node.getDiffuse(), GL11.GL_DIFFUSE);
    renderMaterial(node.getSpecular(), GL11.GL_SPECULAR);
  }

  public void renderMaterial(Vector diffuse, int type) {
    if ( diffuse != null ) {
      GL11.glMaterial(GL11.GL_FRONT, type, colorFactory.getColor(diffuse));
    }
  }

  public void bindTexture(Node node) {
    if ( node.getTextureName() != null ) {
      textureCache.get(node.getTextureName(), () -> loadTexture(node.getTextureName())).bind(0);
    } else {
      GL11.glBindTexture(GL12.GL_TEXTURE_3D, 0);
    }
  }

  private GLTexture loadTexture(String textureName) {
    GLTexture texture = new GLTexture(eventBus);
    texture.setImage(keyReader.getImage(textureName));
    return texture;
  }

  public Matrix updateTransform(Node node, String animName, double alpha, Matrix tr, boolean includeBaseTransforms) {
    AnimationNode animationNode = node.getAnimations().get(animName);
    Vector position = Vector.Z;
    Quaternion rotation = Quaternion.ZERO;

    if ( animationNode != null && animationNode.getPositions().size() > 0) {
      position = position.plus(interpPosition(animationNode.getPositions(), alpha));
    }
    else if ( includeBaseTransforms && node.getPosition() != null ) {
      position = position.plus(node.getPosition());
    }

    if ( animationNode != null && animationNode.getRotations().size() > 0) {
      rotation = rotation.times(interpRotation(animationNode.getRotations(), alpha));
    }
    else if ( includeBaseTransforms && node.getRotation() != null ) {
      rotation = rotation.times(node.getRotation());
    }
    tr = tr.times(Matrix.translate(position));
    tr = tr.times(rotation.toMatrix());
    return tr;
  }

  private void renderFace(Node node, String animName, Face face, Matrix tr, String textureName) {
    GL11.glBegin(GL11.GL_TRIANGLES);
    Vector normal = face.getNormal();
    Vector[] colors = face.getColors();
    Vector[] vs = face.getVertices();
    Vector[] tps = face.getTexturePoints();
    for(int i=0;i<vs.length; ++i) {
      GL11.glNormal3d(normal.x(), normal.y(), normal.z());
      GL11.glColor3d(colors[i].x(), colors[i].y(), colors[i].z());
      if ( tps != null && tps[i] != null && textureName != null ) {
        Vector t = tps[i];
        GL11.glTexCoord2d(t.x(), t.y());
      }
      Vector v = tr.times(vs[i]);
      GL11.glVertex3d(v.x(), v.y(), v.z());
    }
    GL11.glEnd();
  }

  private Quaternion interpRotation(List<RotationTiming> rotations, double alpha) {
    Interp interp = getInterp(alpha, rotations);
    Quaternion q1 = rotations.get(interp.i1).rotation;  
    Quaternion q2 = rotations.get(interp.i2).rotation;
    return interpRotation(q1, q2, interp.delta);
  }

  private Quaternion interpRotation(Quaternion v1, Quaternion v2, double delta) {
    double a = v1.minus(v2).lengthSquared();
    double b = v1.plus(v2).lengthSquared();
    double sign = 1;
    if ( a > b ) {
      sign = -1;
    }
    
    double cosom = v1.dot(v2) * sign;
    double s1, s2;
    if ((1.0f + cosom) > 0.00000001f) {
      if ((1.0f - cosom) > 0.00000001f) {
        double omega = Math.acos(cosom);
        double sinom = Math.sin(omega);
        s1 = Math.sin((1 - delta)*omega) / sinom;
        s2 = Math.sin(delta*omega) / sinom * sign;
      } else {
        s1 = 1.0f - delta;
        s2 = delta * sign;
      }
      return v1.times(s1).plus(v2.times(s2));
    } else {
      Quaternion v3 = new Quaternion(-v1.get(1), -v1.get(1), -v1.get(3), -v1.get(3));
      s1 = Math.sin((1 - delta)*0.5*Math.PI);
      s2 = Math.sin(delta*0.5*Math.PI);
      Quaternion v4 = v1.times(s1).plus(v3.times(s2));
      return new Quaternion(v4.get(0), v4.get(1), v4.get(2), v3.get(3));
    }
  }

  private Vector interpPosition(List<PositionTiming> positions, double alpha) {
    Interp interp = getInterp(alpha, positions);
    Vector v1 = positions.get(interp.i1).position;  
    Vector v2 = positions.get(interp.i2).position;  
    return v1.times(1 - interp.delta).plus(v2.times(interp.delta));
  }
  
  private double getDelta(double alpha, int i1, int i2, List<? extends Timing> positionTimings) {
    double delta;
    if ( i2 > i1) {
      delta =
        (alpha - positionTimings.get(i1).timing) / (positionTimings.get(i2).timing - positionTimings.get(i1).timing);
    }
    else {
      double v1 = 1.0 - positionTimings.get(i1).timing;
      delta = (alpha - positionTimings.get(i1).timing) / v1;
    }
    if ( delta > 1.0 || delta < 0.0 ) {
      throw new RuntimeException("Invalid delta " + delta);
    }
    return delta;
   
  }

  private int getUpperBound(double alpha, List<? extends Timing> positionTimings) {
    for(int i=1; i<positionTimings.size(); ++i) {
      if ( positionTimings.get(i).timing > alpha ) {
        return i - 1;
      }
    }
    return positionTimings.size() - 1;
  }
  
  static class Interp {
    int i1, i2;
    double delta;
  }

  private Interp getInterp(double alpha, List<? extends Timing> timings) {
    Interp i = new Interp();
    i.i1 = getUpperBound(alpha, timings);
    i.i2 = (i.i1 + 1) % timings.size();
    i.delta = getDelta(alpha, i.i1, i.i2, timings);
    return i;
  }

}
