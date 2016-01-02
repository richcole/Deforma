package game;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class CompiledMesh implements ModelResource {

  private SimpleProgram simpleProgram;
  private List<GLTexture> textures;

  private GLVertexArray vao;
  private GLBuffer nbo, vbo, tbo, ibo;

  private int normal, vert, texCoords;

  private Geom geom;

  private boolean wireFrame = false;
  private boolean visible = true;

  public CompiledMesh(SimpleProgram simpleProgram, Geom geom) {
    this.simpleProgram = simpleProgram;
    this.geom = geom;

    vert = simpleProgram.getVert();
    normal = simpleProgram.getNormal();
    texCoords = simpleProgram.getTexCoords();

    Preconditions.checkArgument(vert >= 0);
    Preconditions.checkArgument(texCoords >= 0);

    vao = new GLVertexArray();

    vbo = new GLBuffer();
    vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, getVertexData());

    nbo = new GLBuffer();
    vao.bindData(normal, GL15.GL_ARRAY_BUFFER, nbo, 3, getNormalData());

    tbo = new GLBuffer();
    vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, getTexCoordData());

    ibo = new GLBuffer();
    ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, getElementData());

    textures = Lists.newArrayList();
    for (TexCoords texCoords : geom.getTexCoords()) {
      GLTexture tex = texCoords.getMaterial().getTexture();
      Preconditions.checkNotNull(tex);
      textures.add(tex);
    }
  }

  private FloatBuffer getVertexData() {
    List<Vector> vertices = geom.getVertices();
    FloatBuffer buf = BufferUtils.createFloatBuffer(vertices.size() * 3);

    for (Vector vector : vertices) {
      buf.put((float) vector.x());
      buf.put((float) vector.y());
      buf.put((float) vector.z());
    }
    buf.flip();

    return buf;
  }

  private FloatBuffer getNormalData() {
    List<Vector> vertices = geom.getNormals();
    FloatBuffer buf = BufferUtils.createFloatBuffer(vertices.size() * 3);

    for (Vector vector : vertices) {
      buf.put((float) vector.x());
      buf.put((float) vector.y());
      buf.put((float) vector.z());
    }
    buf.flip();

    return buf;
  }

  private FloatBuffer getTexCoordData() {
    List<TexCoords> texCoordList = geom.getTexCoords();
    int numVertices = geom.getVertices().size();
    FloatBuffer buf = BufferUtils.createFloatBuffer(numVertices * 2
        * texCoordList.size());

    for (TexCoords texCoords : texCoordList) {
      for (int i = 0; i < numVertices; ++i) {
        Vector uv = texCoords.get(i);
        buf.put((float) uv.x());
        buf.put((float) uv.y());
      }
    }
    buf.flip();

    return buf;
  }

  private IntBuffer getElementData() {
    List<Integer> indexes = geom.getElements();
    IntBuffer buf = BufferUtils.createIntBuffer(indexes.size());

    for (Integer index : indexes) {
      buf.put(index);
    }
    buf.flip();

    return buf;
  }

  public void render(Matrix modelTr) {
    if (visible) {
      simpleProgram.use();

      int textureIndex = 0;
      for (GLTexture tex : textures) {
        tex.bind(textureIndex);
        textureIndex += 1;
      }
      simpleProgram.setModelTr(modelTr);

      GL30.glBindVertexArray(vao.getId());
      GL20.glEnableVertexAttribArray(vert);
      GL20.glEnableVertexAttribArray(texCoords);

      if (wireFrame) {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
      } else {
        GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
      }
      ibo.drawElements();
    }
  }

  public void setWireFrame(boolean wireFrame) {
    this.wireFrame = wireFrame;
  }

  public void setVisible(boolean visible) {
    this.visible = visible;
  }

  public Geom getGeom() {
    return geom;
  }
}
