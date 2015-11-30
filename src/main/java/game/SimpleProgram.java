package game;

import org.lwjgl.opengl.GL20;

public class SimpleProgram {

  private GLProgram program;
  private int vert, viewTr, modelTr, texCoords;

  public SimpleProgram() {
    program = new GLProgram();
    program.attach(new GLShader(GL20.GL_VERTEX_SHADER).compile("simple.vert"));
    program
        .attach(new GLShader(GL20.GL_FRAGMENT_SHADER).compile("simple.frag"));
    program.link();
    program.setUniform("tex", 0);
    vert = program.getAttrib("vert");
    texCoords = program.getAttrib("texCoords");
    viewTr = program.getUniform("viewTr");
    modelTr = program.getUniform("modelTr");
  }

  public int getVert() {
    return vert;
  }

  public void use() {
    program.use();
  }

  public int getTexCoords() {
    return texCoords;
  }

  public void setViewTr(Matrix viewMatrix) {
    if (viewMatrix != null) {
      GL20.glUniformMatrix4(viewTr, true, viewMatrix.toBuf());
    } else {
      GL20.glUniformMatrix4(viewTr, true, Matrix.IDENTITY.toBuf());
    }
  }

  public void setModelTr(Matrix modelMatrix) {
    if (modelMatrix != null) {
      GL20.glUniformMatrix4(modelTr, true, modelMatrix.toBuf());
    } else {
      GL20.glUniformMatrix4(modelTr, true, Matrix.IDENTITY.toBuf());
    }
  }
}