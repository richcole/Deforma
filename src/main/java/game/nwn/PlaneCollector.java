package game.nwn;

import game.Context;
import game.base.Face;
import game.base.Texture;
import game.math.Matrix;
import game.math.Quaternion;
import game.math.Vector;
import game.nwn.Mesh.Visitor;
import game.nwn.readers.MdlFace;
import game.nwn.readers.MdlMeshHeader;
import game.nwn.readers.MdlNodeHeader;

import java.util.List;
import java.util.Stack;

import com.google.common.collect.Lists;

public class PlaneCollector implements Visitor {
  
  List<Face>   faces = Lists.newArrayList();
  Stack<Matrix> trFroms = new Stack<Matrix>();
  Matrix tr = Matrix.IDENTITY;
  Context context;
  
  public PlaneCollector(Context context) {
    this.context = context;
    trFroms.push(tr);
  }
  
  static class FrameInterp {
    int i1, i2;
    float delta;
  }
  
  @Override
  public void preVisit(MdlNodeHeader node, MdlNodeHeader fromNode, float alpha) {
    MdlMeshHeader meshHeader = node.getMeshHeader();
    trFroms.push(tr);
    
    if ( fromNode.getPosition() != null ) {
      FrameInterp i = getInterp(alpha, fromNode.getPositionTimings());
      tr = tr.times(Matrix.translate(mixPosition(fromNode.getPosition()[i.i1], fromNode.getPosition()[i.i2], i.delta)));
    }
    else if ( node.getPosition() != null ) {
      tr = tr.times(Matrix.translate(node.getPosition()[0]));
    }
    
    if ( fromNode.getOrientation() != null ) {
      FrameInterp i = getInterp(alpha, fromNode.getOrientationTimings());
      tr = tr.times(mixOrientation(fromNode.getOrientation()[i.i1], fromNode.getOrientation()[i.i2], i.delta).toMatrix());
    }
    else if ( node.getOrientation() != null ) {
      tr = tr.times(node.getOrientation()[0].toMatrix());
    }
    
    if ( meshHeader != null ) {
      Vector[] vertexes = meshHeader.getVertices();
      Vector diffuse = meshHeader.getDiffuse();
      Vector specular = meshHeader.getSpecular();
      Vector[] colors = meshHeader.getColors();
      Texture[] textures = new Texture[4];
      for(int i=0;i<4;++i) {
        String textureName = meshHeader.getTextures()[i]; 
        if ( textureName.length() > 0 ) {
          textures[i] = context.getTextures().getTexture(textureName, new TextureProvider(context, textureName));
          if ( i > 0 ) {
            int a = 1;
          }
        }
      }
      for(MdlFace mdlFace: meshHeader.getFaces()) {
         int[] vertex = mdlFace.getVertex();
         Vector[][] texturePoints = meshHeader.getTexturePoints();
         Vector[] vs = new Vector[3];
         Vector[] tps = new Vector[3];
         for(int i=0;i<3;++i) {
           Vector vx = vertexes[vertex[i]];
           vs[i] = tr.times(vx);
           tps[i] = texturePoints[0][vertex[i]];
         }
         faces.add(new Face(vs, colors, diffuse, specular, mdlFace.getPlaneNormal(), textures[0], tps));
      }
    }
  }

  private FrameInterp getInterp(float alpha, float[] timings) {
    FrameInterp i = new FrameInterp();
    i.i1 = getUpperBound(alpha, timings);
    i.i2 = (i.i1 + 1) % timings.length;
    i.delta = getDelta(alpha, i.i1, i.i2, timings);
    return i;
  }

  private Vector mixPosition(Vector v1, Vector v2, float delta) {
    return v1.times(1 - delta).plus(v2.times(delta));
  }

  private Quaternion mixOrientation(Quaternion v1, Quaternion v2, float delta) {
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

  private float getDelta(float alpha, int i1, int i2, float[] positionTimings) {
    float delta;
    if ( i2 > i1) {
      delta =
        (alpha - positionTimings[i1]) / (positionTimings[i2] - positionTimings[i1]);
    }
    else {
      float v1 = 1.0f - positionTimings[i1];
      delta = (alpha - positionTimings[i1]) / v1;
    }
    if ( delta > 1.0 || delta < 0.0 ) {
      throw new RuntimeException("Invalid delta " + delta);
    }
    return delta;
   
  }

  private int getUpperBound(float alpha, float[] positionTimings) {
    for(int i=1; i<positionTimings.length; ++i) {
      if ( positionTimings[i] > alpha ) {
        return i - 1;
      }
    }
    return positionTimings.length - 1;
  }

  public List<Face> getFaces() {
    return faces;
  }

  @Override
  public void postVisit(MdlNodeHeader node, MdlNodeHeader fromNode, float alpha) {
    tr = trFroms.pop();
  }
  
  

}
