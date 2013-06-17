package game.models;

import game.Context;
import game.base.Face;
import game.math.Matrix;
import game.math.Vector;
import game.models.AnimMesh.Node;
import game.utils.GLUtils;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

import com.google.common.collect.Lists;

public class CompressedAnimMesh {
  
  List<CNode> cNodes = Lists.newArrayList();
  AnimMesh animMesh;
  CNode root;
  Context context;
  int faceCount = 0;
  
  public class CNode {
    int         size;
    Matrix      tr;
    FloatBuffer pos;
    FloatBuffer color;
    FloatBuffer normal;
    FloatBuffer texCoords;
    
    int vboIdPos;
    int vboIdColor;
    int vboIdNormal;
    int vboIdTexCoords;
    
    List<Face>  faces = Lists.newArrayList();
    List<CNode> children = Lists.newArrayList();
    Node        node;
    String      textureName;
    private Vector diffuse;
    private Vector specular;
    
    public void addFaces(List<Face> faces, Matrix tr) {
      for(Face face: faces) {
        this.faces.add(face.transform(tr));
      }
    }
    
    public void freeze() {
      size = faces.size();

      if ( size > 0 ) {
        allocateBuffers();
        bindArrays();
      }
    }

    private void allocateBuffers() {
      pos = BufferUtils.createFloatBuffer(size*9);
      color = BufferUtils.createFloatBuffer(size*9);
      normal = BufferUtils.createFloatBuffer(size*9);
      texCoords = BufferUtils.createFloatBuffer(size*9);
      
      for(Face face: faces) {
        for(int i=0;i<3;++i) {
          putVertex(pos, face.getVertices()[i]);
          putVertex(color, face.getColors()[i]);
          putVertex(normal, face.getNormal());
          putVertex(texCoords, face.getTexturePoints()[i]);
        }
      }
      
      pos.flip();
      color.flip();
      normal.flip();
      texCoords.flip();
    }

    private void putVertex(FloatBuffer buf, Vector v) {
      if ( v != null ) {
        buf.put((float)v.x());
        buf.put((float)v.y());
        buf.put((float)v.z());
      }
      else {
        buf.put(0f);
        buf.put(0f);
        buf.put(0f);
      }
    }

    private void bindArrays() {
      vboIdPos = createArray(0, pos);
      vboIdColor = createArray(1, color);
      vboIdNormal = createArray(2, normal);
      vboIdTexCoords = createArray(3, texCoords);
    }
       
    public void render(AnimMeshRenderer renderer) {
      faceCount += size;
      if ( size > 0 ) {
        GLUtils.pushMatrix(tr);
        if ( textureName != null ) {
          context.getTextures().getFileTexture(textureName + ".tga").bind();
        } else {
          GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        }
        renderer.renderMaterial(diffuse, GL11.GL_DIFFUSE);
        renderer.renderMaterial(specular, GL11.GL_SPECULAR);
        
        GL11.glEnableClientState(GL11.GL_VERTEX_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboIdPos);
        GL11.glVertexPointer(3, GL11.GL_FLOAT, 0, 0);
  
        GL11.glEnableClientState(GL11.GL_COLOR_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboIdColor);
        GL11.glColorPointer(3, GL11.GL_FLOAT, 0, 0);
  
        GL11.glEnableClientState(GL11.GL_NORMAL_ARRAY);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboIdNormal);
        GL11.glNormalPointer(GL11.GL_FLOAT, 0, 0);
  
        if ( textureName != null ) {
          GL11.glEnableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
          GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboIdTexCoords);
          GL11.glTexCoordPointer(3, GL11.GL_FLOAT, 0, 0);
        } else {
          int x = 1;
        }
  
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, size*3);
        GL11.glDisableClientState(GL11.GL_VERTEX_ARRAY);
        GL11.glDisableClientState(GL11.GL_COLOR_ARRAY);
        GL11.glDisableClientState(GL11.GL_NORMAL_ARRAY);
        GL11.glDisableClientState(GL11.GL_TEXTURE_COORD_ARRAY);
        GL11.glPopMatrix();
      }
    }

    private int createArray(int index, FloatBuffer buf) {
      int vboId = GL15.glGenBuffers();
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboId);
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buf, GL15.GL_STATIC_DRAW);
      return vboId;
    }

    public void setTransform(Matrix tr) {
      this.tr = tr;
    }

    public Node getNode() {
      return node;
    }

    public void setNode(Node node) {
      this.node = node;
    }

    public void addChild(CNode cNode) {
      this.children.add(cNode);
    }

    public List<CNode> getChildren() {
      return children;
    }

    public void setTextureName(String textureName) {
      this.textureName = textureName;
    }

    public String getTextureName() {
      return textureName;
    }

    public void setDiffuse(Vector diffuse) {
      this.diffuse = diffuse;
    }
    
    public void setSpecular(Vector specular) {
      this.specular = specular;
    }

    public List<Face> getFaces() {
      return faces;
    }
    
  }
  
  public CompressedAnimMesh(Context context, AnimMesh animMesh) {
    this.context = context;
    this.animMesh = animMesh;
    this.root = newCNode();
    processNode(animMesh.getRoot(), root, Matrix.IDENTITY);
    for(CNode cNode: cNodes) {
      cNode.freeze();
    }
  }
  
  public void update(AnimMeshRenderer renderer, String animName, double alpha) {
    updateNode(root, Matrix.IDENTITY, renderer, animName, alpha);
  }
  
  private CNode newCNode() {
    CNode cNode = new CNode();
    cNodes.add(cNode);
    return cNode;
  }

  private void processNode(Node node, CNode parentCNode, Matrix tr) {
    
    CNode cNode;
    
    if ( parentCNode.getNode() == null ) {
      cNode = parentCNode;
      cNode.setNode(node);
    } else if ( hasAnim(node) || hasDifferentTexture(node, parentCNode) ) {
      cNode = newCNode();
      parentCNode.addChild(cNode);
      cNode.setNode(node);
      tr = Matrix.IDENTITY;
    } else {
      cNode = parentCNode;
    }

    tr = tr.times(node.getTransform());
    if ( node.getTextureName() != null ) {
      cNode.setTextureName(node.getTextureName());
    }
    if ( node.getDiffuse() != null ) {
      cNode.setDiffuse(node.getDiffuse());
    }
    if ( node.getSpecular() != null ) {
      cNode.setSpecular(node.getSpecular());
    }

    cNode.addFaces(node.getFaces(), tr);
    
    for(Node child: node.getChildren()) {
      processNode(child, cNode, tr);
    }
    
  }
  
  private boolean hasDifferentTexture(Node node, CNode cNode) {
    if ( cNode.getTextureName() == null ) {
      if ( cNode.getFaces().size() == 0 ) {
        return false;
      } else {
        return node.getTextureName() != null;
      }
    } else {
      if ( node.getTextureName() == null ) {
        return node.getFaces().size() > 0;
      } else {
        return ! cNode.getTextureName().equals(node.getTextureName());
      }
    }
  }

  public void updateNode(CNode cNode, Matrix tr, AnimMeshRenderer renderer, String animName, double alpha) {
    tr = renderer.updateTransform(cNode.getNode(), animName, alpha, tr, false);
    cNode.setTransform(tr);
    
    for(CNode child: cNode.getChildren()) {
      updateNode(child, tr, renderer, animName, alpha);
    }
    
  }

  private boolean hasAnim(Node node) {
    return node.getAnimations().size() > 0;
  }

  public void render(AnimMeshRenderer renderer) {
    faceCount = 0;
    for(CNode cNode: cNodes) {
      cNode.render(renderer);
    }
    int x = 1;
  }

  public AnimMesh getAnimMesh() {
    return animMesh;
  }

}
