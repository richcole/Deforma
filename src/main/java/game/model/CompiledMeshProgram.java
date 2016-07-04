package game.model;

import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.Util;

import game.gl.GLFactory;
import game.gl.GLProgram;
import game.math.Matrix;

public class CompiledMeshProgram {

	private GLProgram program;
	private int p1Binding;
	private int p2Binding;
	private int normalBinding;
	private int texCoordBinding;
	private int viewTrBinding;
	private int modelTrBinding;
	private int alphaBinding;

	public CompiledMeshProgram(GLFactory glFactory) {
		String baseName = "simple";
		program = glFactory.newProgram();
		program.attach(glFactory.newShader(GL20.GL_VERTEX_SHADER).compile(baseName + ".vert"));
		program.attach(glFactory.newShader(GL20.GL_FRAGMENT_SHADER).compile(baseName + ".frag"));
		program.link();
		Util.checkGLError();
		// program.setUniform("tex", 0);
		Util.checkGLError();
		p1Binding = program.getAttrib("p1");
		p2Binding = program.getAttrib("p2");
		normalBinding = program.getAttrib("normal");
		texCoordBinding = program.getAttrib("texCoords");
		viewTrBinding = program.getUniform("viewTr");
		modelTrBinding = program.getUniform("modelTr");
		alphaBinding = program.getUniform("alpha");
	}

	public int getP1Binding() {
		return p1Binding;
	}

	public int getP2Binding() {
		return p2Binding;
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

	public void setAlpha(float alpha) {
		GL20.glUniform1f(alphaBinding, alpha);
	}

	public void use() {
		program.use();
	}
	
	public GLProgram getProgram() {
		return program;
	}

}
