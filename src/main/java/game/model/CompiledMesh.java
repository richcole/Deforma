package game.model;

import game.gl.GLBuffer;
import game.gl.GLFactory;
import game.gl.GLTexture;
import game.gl.GLVertexArray;
import game.math.Matrix;
import game.math.Utils;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class CompiledMesh {
  
  private GLVertexArray vao;
  private GLBuffer vbo;
  private GLBuffer nbo;
  private GLBuffer tbo;
  private GLBuffer ibo;

  private GLTexture tex;
  private CompiledMeshProgram program;

  public CompiledMesh(GLFactory glFactory, CompiledMeshProgram compiledProgram, CompiledTexture compiledTexture, Mesh mesh) {
    this.program = compiledProgram;
    
    double t[] = compiledTexture.getCompositeImage().transform(mesh);
    tex = compiledTexture.getTex();
    
    vao = glFactory.newVertexArray();

    vbo = glFactory.newBuffer();
    vao.bindData(program.getVertexBinding(), GL15.GL_ARRAY_BUFFER, vbo, 3, Utils.toFloatBuffer(mesh.p));
    
    nbo = glFactory.newBuffer();
    vao.bindData(program.getNormalBinding(), GL15.GL_ARRAY_BUFFER, nbo, 3, Utils.toFloatBuffer(mesh.n));

    tbo = glFactory.newBuffer();
    vao.bindData(program.getTexCoordBinding(), GL15.GL_ARRAY_BUFFER, tbo, 2, Utils.toFloatBuffer(t));
    
    ibo = glFactory.newBuffer();
    ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utils.toIntBuffer(mesh.e));
  }
  
  public void render(Matrix modelTr) {
    program.setModelTr(modelTr);
    GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
    // GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);    
    GL30.glBindVertexArray(vao.getId());
    GL20.glEnableVertexAttribArray(program.getVertexBinding());
    // GL20.glEnableVertexAttribArray(program.getNormalBinding());
    GL20.glEnableVertexAttribArray(program.getTexCoordBinding());
    tex.bind(0);
    ibo.drawElements();
  }
}
