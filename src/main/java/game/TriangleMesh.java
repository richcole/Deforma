package game;

import org.lwjgl.opengl.GL15;

import com.google.common.base.Preconditions;

public class TriangleMesh implements ModelResource {

  GLVertexArray vao;
  GLBuffer vbo, tbo;

  static float[] verticesData = { 0.0f, 0.8f, 0.0f, -0.8f, -0.8f, 0.0f, 0.8f,
      -0.8f, 0.0f };

  static float[] texCoordsData = { 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, };

  private SimpleProgram simpleProgram;
  private TextureSupplier texSupplier;

  private int vert;
  private int texCoords;
  private GLTexture tex;

  public TriangleMesh(SimpleProgram simpleProgram, TextureSupplier tex) {
    this.simpleProgram = simpleProgram;
    this.texSupplier = tex;

    vert = simpleProgram.getVert();
    texCoords = simpleProgram.getTexCoords();
    Preconditions.checkArgument(vert >= 0);
    Preconditions.checkArgument(texCoords >= 0);
    vao = new GLVertexArray();

    vbo = new GLBuffer();
    vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3,
        Utils.toFloatBuffer(verticesData));

    tbo = new GLBuffer();
    vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2,
        Utils.toFloatBuffer(verticesData));

    this.tex = texSupplier.getTexture();
  }

  public void dispose() {
  }

  public void render(Matrix modelTr) {
    simpleProgram.use();
    tex.bind(0);
    simpleProgram.setModelTr(modelTr);
    vao.drawArrays();
  }

}
