package game;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;

public class CompiledAnimMesh implements Model {

  GLVertexArray vao;
  GLBuffer vbo, tbo, ibo, bbo;

  private AnimProgram program;
  private TextureSupplier texSupplier;
  private int vert, texCoords, boneIndex;
  private Geom geom;
  private GLTexture tex;
  private Matrix modelTr;

  public CompiledAnimMesh(AnimProgram program, TextureSupplier tex, Geom geom) {
    this.program = program;
    this.texSupplier = tex;
    this.geom = geom;
    this.modelTr = Matrix.IDENTITY;

    vert = program.getVert();
    texCoords = program.getTexCoords();

    Preconditions.checkArgument(vert >= 0);
    Preconditions.checkArgument(texCoords >= 0);

    vao = new GLVertexArray();

    vbo = new GLBuffer();
    vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, getVertexData());

    tbo = new GLBuffer();
    vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, getTexCoordData());

    bbo = new GLBuffer();
    vao.bindData(boneIndex, GL15.GL_ARRAY_BUFFER, bbo, 2, getBoneIndexData());

    ibo = new GLBuffer();
    ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, getElementData());

    this.tex = texSupplier.getTexture();
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

  private FloatBuffer getBoneIndexData() {
    List<Integer> bones = geom.getBones();
    FloatBuffer buf = BufferUtils.createFloatBuffer(bones.size());

    for (Integer bone : bones) {
      buf.put((float) bone);
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

  public void dispose() {
  }

  public void render(Matrix modelTr) {
    program.use();
    tex.bind(program.getTex());

    GL30.glBindVertexArray(vao.getId());
    GL20.glEnableVertexAttribArray(vert);
    GL20.glEnableVertexAttribArray(texCoords);

    ibo.drawElements();
  }

}
