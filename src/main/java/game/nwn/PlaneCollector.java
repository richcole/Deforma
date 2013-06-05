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
  Stack<Matrix> trTos = new Stack<Matrix>();
  Matrix trFrom = Matrix.IDENTITY;
  Matrix trTo = Matrix.IDENTITY;
  Context context;
  
  public PlaneCollector(Context context) {
    this.context = context;
    trFroms.push(trFrom);
    trTos.push(trTo);
  }
  
  @Override
  public void preVisit(MdlNodeHeader node, MdlNodeHeader fromNode, MdlNodeHeader toNode, float alpha) {
    MdlMeshHeader meshHeader = node.getMeshHeader();
    trFroms.push(trFrom);
    trTos.push(trTo);
    
    if ( fromNode.getPosition() != null ) {
      trFrom = trFrom.times(Matrix.translate(fromNode.getPosition()));
    }
    else if ( node.getPosition() != null ) {
      trFrom = trFrom.times(Matrix.translate(node.getPosition()));
    }
    if ( toNode.getPosition() != null ) {
      trTo = trTo.times(Matrix.translate(toNode.getPosition()));
    }
    else if ( node.getPosition() != null ) {
      trTo = trTo.times(Matrix.translate(node.getPosition()));
    }
    
    if ( fromNode.getOrientation() != null ) {
      trFrom = trFrom.times(fromNode.getOrientation().toMatrix());
    }
    else if ( node.getOrientation() != null ) {
      trFrom = trFrom.times(node.getOrientation().toMatrix());
    }
    if ( toNode.getOrientation() != null ) {
      trTo = trTo.times(toNode.getOrientation().toMatrix());
    }
    else if ( node.getOrientation() != null ) {
      trTo = trTo.times(node.getOrientation().toMatrix());
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
           vs[i] = trFrom.times(vx).times(1 - alpha).plus(trTo.times(vx).times(alpha));
           tps[i] = texturePoints[0][vertex[i]];
         }
         faces.add(new Face(vs, colors, diffuse, specular, mdlFace.getPlaneNormal(), textures[0], tps));
      }
    }
  }

  public List<Face> getFaces() {
    return faces;
  }

  @Override
  public void postVisit(MdlNodeHeader node, MdlNodeHeader fromNode, MdlNodeHeader toNode, float alpha) {
    trFrom = trFroms.pop();
    trTo = trTos.pop();
  }
  
  

}
