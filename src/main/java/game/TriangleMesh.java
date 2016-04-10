package game;

import game.basicgeom.Matrix;
import game.events.EventBus;
import game.gl.GLBuffer;
import game.gl.GLTexture;
import game.gl.GLVertexArray;
import game.image.Material;

import org.lwjgl.opengl.GL15;

import com.google.common.base.Preconditions;

public class TriangleMesh implements ModelResource {

  GLVertexArray vao;
  GLBuffer vbo, tbo;

  static float[] verticesData = { 0.0f, 0.8f, 0.0f, -0.8f, -0.8f, 0.0f, 0.8f,
      -0.8f, 0.0f };

  static float[] texCoordsData = { 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, };

  private SimpleProgram simpleProgram;
  private Material texSupplier;

  private int vert;
  private int texCoords;
  private GLTexture tex;

  public TriangleMesh(EventBus eventBus, SimpleProgram simpleProgram, Material tex) {
    this.simpleProgram = simpleProgram;
    this.texSupplier = tex;

    vert = simpleProgram.getVert();
    texCoords = simpleProgram.getTexCoords();
    Preconditions.checkArgument(vert >= 0);
    Preconditions.checkArgument(texCoords >= 0);
    vao = new GLVertexArray(eventBus);

    vbo = new GLBuffer(eventBus);
    vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3,
        Utils.toFloatBuffer(verticesData));

    tbo = new GLBuffer(eventBus);
    vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2,
        Utils.toFloatBuffer(verticesData));

    this.tex = texSupplier.getTexture();
  }

  public void render(Matrix modelTr) {
    simpleProgram.use();
    tex.bind(0);
    simpleProgram.setModelTr(modelTr);
    vao.drawArrays();
  }

}
