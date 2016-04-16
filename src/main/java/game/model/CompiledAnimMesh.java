package game.model;

import game.gl.GLBuffer;
import game.gl.GLFactory;
import game.gl.GLTexture;
import game.gl.GLVertexArray;
import game.math.Matrix;
import game.math.Utils;
import game.model.UniformBindingPool.UniformBinding;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import com.google.common.base.Preconditions;

public class CompiledAnimMesh implements TransformRenderable {

	private GLVertexArray vao;
	private GLBuffer vbo;
	private GLBuffer nbo;
	private GLBuffer tbo;
	private GLBuffer bbo;
	private GLBuffer ibo;
	private GLBuffer btrbo;
	private UniformBinding btrboBinding;

	private int numBones;
	private int frame;

	private GLTexture tex;
	private CompiledAnimMeshProgram program;

	public CompiledAnimMesh(GLFactory glFactory, UniformBindingPool bindingPool,
			CompiledAnimMeshProgram compiledProgram, CompiledTexture compiledTexture, AnimMesh mesh) {
		this.program = compiledProgram;

		double t[] = compiledTexture.getCompositeImage().transform(mesh.nv, mesh.i, mesh.imageList, mesh.t);
		tex = compiledTexture.getTex();

		vao = glFactory.newVertexArray();

		vbo = glFactory.newBuffer();
		vao.bindData(program.getVertBinding(), GL15.GL_ARRAY_BUFFER, vbo, 3, Utils.toFloatBuffer(mesh.p));

		nbo = glFactory.newBuffer();
		vao.bindData(program.getNormalBinding(), GL15.GL_ARRAY_BUFFER, nbo, 3, Utils.toFloatBuffer(mesh.n));

		tbo = glFactory.newBuffer();
		vao.bindData(program.getTexCoordBinding(), GL15.GL_ARRAY_BUFFER, tbo, 2, Utils.toFloatBuffer(t));

		bbo = glFactory.newBuffer();
		vao.bindData(program.getBoneBinding(), GL15.GL_ARRAY_BUFFER, bbo, 1, Utils.toFloatBuffer(mesh.b));

		numBones = mesh.numBones;

		Preconditions.checkArgument(mesh.bTr.length <= 16 * 100000);
		btrboBinding = bindingPool.createBinding();
		btrbo = glFactory.newBuffer();
		btrbo.bindUniformData(GL31.GL_UNIFORM_BUFFER, btrboBinding, Utils.toFloatBuffer(mesh.bTr));

		ibo = glFactory.newBuffer();
		ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utils.toIntBuffer(mesh.e));
	}

	public void render(Matrix viewTr, Matrix modelTr) {
		program.use();
		program.setViewTr(viewTr);
		program.setModelTr(modelTr);
		program.setBoneTr(btrbo, btrboBinding);
		program.setFrame(frame);
		program.setNumBones(numBones);
		GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL30.glBindVertexArray(vao.getId());
		GL20.glEnableVertexAttribArray(program.getVertBinding());
		GL20.glEnableVertexAttribArray(program.getNormalBinding());
		GL20.glEnableVertexAttribArray(program.getTexCoordBinding());
		tex.bind(0);
		ibo.drawElements();
	}
}
