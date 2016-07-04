package game.gl;

import game.math.Utils;

import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL31;
import org.lwjgl.opengl.Util;

import com.google.common.base.Preconditions;

public class GLProgram extends GLResource {

	int id;

	public GLProgram(Disposer disposer) {
    super(disposer);
		this.id = GL20.glCreateProgram();
	}

	protected Runnable dispose() {
	  return () -> GL20.glDeleteProgram(id);
	}

	public void attach(GLShader shader) {
		GL20.glAttachShader(id, shader.getId());
	}

	public void link() {
		GL20.glLinkProgram(id);
		if (GL20.glGetProgram(id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Unable to link program" + ":"
					+ GL20.glGetProgramInfoLog(id, 4 * 1024));
		}
	}

	public void use() {
		GL20.glUseProgram(id);
	}

	public int getId() {
		return id;
	}

	public int getAttrib(String name) {
		int attribId = GL20.glGetAttribLocation(id, name);
		Util.checkGLError();
		Preconditions.checkArgument(attribId != GL31.GL_INVALID_INDEX);
		return attribId;
	}

	public int getUniform(String name) {
		int attribId = GL20.glGetUniformLocation(id, name);
		Preconditions.checkArgument(attribId != GL31.GL_INVALID_INDEX);
		return attribId;
	}

	public int getUniformBlockIndex(String name) {
		int attribId = GL31.glGetUniformBlockIndex(id, name);
		Preconditions.checkArgument(attribId != GL31.GL_INVALID_INDEX);
		return attribId;
	}

	public void setUniform(String name, int value) {
		int uniformLocation = GL20.glGetUniformLocation(id, name);
		Preconditions.checkArgument(uniformLocation != GL31.GL_INVALID_INDEX);
		GL20.glUniform1i(uniformLocation, value);
	}

	public void setUniform(int var, int value) {
		GL20.glUniform1i(var, value);
	}

	public void setUniform(int var, float value) {
		GL20.glUniform1f(var, value);
	}

	public void setUniformInts(String name, List<Integer> values) {
		GL20.glUniform1(GL20.glGetUniformLocation(id, name),
				Utils.toIntBuffer(values));
	}

	public void setUniformInts(int var, List<Integer> values) {
		GL20.glUniform1(var, Utils.toIntBuffer(values));
	}

	public void setUniformFloats(int var, List<Float> values) {
		GL20.glUniform1(var, Utils.toFloatBuffer(values));
	}

	public void setUniformFloats(int var, FloatBuffer values) {
		GL20.glUniform1(var, values);
	}
}
