package game.model;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;

import game.gl.GLBuffer;
import game.gl.GLFactory;
import game.gl.GLProgram;
import game.math.Matrix;
import game.model.UniformBindingPool.UniformBinding;

public class CompiledAnimMeshProgram {

	private GLProgram program;
	private int vertBinding;
	private int normalBinding;
	private int texCoordBinding;
	private int viewTrBinding;
	private int modelTrBinding;

	private int boneBinding;
	private int boneTrBlockIndex;
	private int numBonesBinding;
	private int frameBinding;

	public CompiledAnimMeshProgram(GLFactory glFactory) {
		String baseName = "anim";
		program = glFactory.newProgram();
		program.attach(glFactory.newShader(GL20.GL_VERTEX_SHADER).compile(baseName + ".vert"));
		program.attach(glFactory.newShader(GL20.GL_FRAGMENT_SHADER).compile(baseName + ".frag"));
		program.link();
		program.setUniform("tex", 0);
		vertBinding = program.getAttrib("vert");
		normalBinding = program.getAttrib("normal");
		texCoordBinding = program.getAttrib("texCoords");
		viewTrBinding = program.getUniform("viewTr");
		modelTrBinding = program.getUniform("modelTr");

		boneBinding = getProgram().getAttrib("bone");
		numBonesBinding = getProgram().getUniform("numBones");
		frameBinding = getProgram().getUniform("frame");
		boneTrBlockIndex = getProgram().getUniformBlockIndex("Bones");
	}

	public int getBoneBinding() {
		return boneBinding;
	}

	public void setBoneTr(GLBuffer btrbo, UniformBinding btrboBinding) {
		GL31.glUniformBlockBinding(getProgram().getId(), boneTrBlockIndex, btrboBinding.getBindingIndex());
	}

	public void setNumBones(int numBones) {
		getProgram().setUniform(numBonesBinding, numBones);
	}

	public void setFrame(int frame) {
		getProgram().setUniform(frameBinding, frame);
	}
	
	public int getVertBinding() {
		return vertBinding;
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
	
	public GLProgram getProgram() {
		return program;
	}
	
}
