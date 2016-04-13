package game.model;

import game.gl.GLFactory;
import game.gl.GLProgram;
import game.math.Matrix;

import org.lwjgl.opengl.GL20;

public class CompiledMeshProgram {
  
  private GLProgram program;
  private int vertexBinding;
  private int normalBinding;
  private int texCoordBinding;
  private int viewTrBinding;
  private int modelTrBinding;

  public CompiledMeshProgram(GLFactory glFactory) {
    program = glFactory.newProgram();
    program.attach(glFactory.newShader(GL20.GL_VERTEX_SHADER).compile("simple.vert"));
    program.attach(glFactory.newShader(GL20.GL_FRAGMENT_SHADER).compile("simple.frag"));
    program.link();
    program.setUniform("tex", 0);
    vertexBinding = program.getAttrib("vert");
    normalBinding = program.getAttrib("normal");
    texCoordBinding = program.getAttrib("texCoords");
    viewTrBinding = program.getUniform("viewTr");
    modelTrBinding = program.getUniform("modelTr");
  }
  
  public int getVertexBinding() {
    return vertexBinding;
  }

  public int getNormalBinding() {
    return normalBinding;
  }

  public int getTexCoordBinding() {
    return texCoordBinding;
  }

  public void setModelTr(Matrix modelTr) {
    if (modelTr == null) {
      modelTr = Matrix.IDENTITY;
    }
    GL20.glUniformMatrix4(modelTrBinding, true, modelTr.toBuf());
  }

  public void setViewTr(Matrix viewTr) {
    if (viewTr == null) {
      viewTr = Matrix.IDENTITY;
    }
    GL20.glUniformMatrix4(viewTrBinding, true, viewTr.toBuf());
  }

  public void use() {
    program.use();
  }
}
