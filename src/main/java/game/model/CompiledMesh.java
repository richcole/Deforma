package game.model;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import game.gl.GLBuffer;
import game.gl.GLFactory;
import game.gl.GLTexture;
import game.gl.GLVertexArray;
import game.math.Matrix;
import game.math.Utils;

public class CompiledMesh implements TransformRenderable {

	private GLVertexArray vao;
	private GLBuffer p1bo;
	private GLBuffer p2bo;
	private GLBuffer nbo;
	private GLBuffer tbo;
	private GLBuffer ibo;

	private GLTexture tex;
	private CompiledMeshProgram program;

	public CompiledMesh(GLFactory glFactory, UniformBindingPool bindingPool, CompiledMeshProgram compiledProgram,
			CompiledTexture compiledTexture, Mesh mesh) {
		this.program = compiledProgram;

		double t[] = compiledTexture.getCompositeImage().transform(mesh.nv, mesh.i, mesh.imageList, mesh.t);
		tex = compiledTexture.getTex();

		vao = glFactory.newVertexArray();

		p1bo = glFactory.newBuffer();
		vao.bindData(program.getP1Binding(), GL15.GL_ARRAY_BUFFER, p1bo, 3, Utils.toFloatBuffer(mesh.p1));

		p2bo = glFactory.newBuffer();
		vao.bindData(program.getP2Binding(), GL15.GL_ARRAY_BUFFER, p2bo, 3, Utils.toFloatBuffer(mesh.p2));

		nbo = glFactory.newBuffer();
		vao.bindData(program.getNormalBinding(), GL15.GL_ARRAY_BUFFER, nbo, 3, Utils.toFloatBuffer(mesh.n));

		tbo = glFactory.newBuffer();
		vao.bindData(program.getTexCoordBinding(), GL15.GL_ARRAY_BUFFER, tbo, 2, Utils.toFloatBuffer(t));

		ibo = glFactory.newBuffer();
		ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, Utils.toIntBuffer(mesh.e));
	}

	public void render(Matrix viewTr, Matrix modelTr, float alpha) {
		program.use();
		program.setViewTr(viewTr);
		program.setModelTr(modelTr);
		program.setAlpha(alpha);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_FILL);
		GL11.glFrontFace(GL11.GL_CCW);
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
		// GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		GL30.glBindVertexArray(vao.getId());
		GL20.glEnableVertexAttribArray(program.getP1Binding());
		GL20.glEnableVertexAttribArray(program.getP2Binding());
		GL20.glEnableVertexAttribArray(program.getNormalBinding());
		GL20.glEnableVertexAttribArray(program.getTexCoordBinding());
		tex.bind(0);
		ibo.drawElements();
	}
	
	@Override
	public void render(Matrix viewTr, Matrix modelTr) {
		render(viewTr, modelTr, 0);
	}
}
