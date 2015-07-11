package game.gl;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import game.Util;
import game.gl.GLShader;

public class GLProgram {

	int id;

	public GLProgram() {
		this.id = GL20.glCreateProgram();
	}

	void finalizer() {
		GL20.glDeleteProgram(id);
	}

	public void attach(GLShader shader) {
		GL20.glAttachShader(id, shader.getId());
	}

	public void link() {
		GL20.glLinkProgram(id);
		if (GL20.glGetProgram(id, GL20.GL_LINK_STATUS) == GL11.GL_FALSE) {
			throw new RuntimeException("Unable to link program" + ":" + GL20.glGetProgramInfoLog(id, 4 * 1024));
		}
	}

	public void use() {
		GL20.glUseProgram(id);
	}

	public int getId() {
		return id;
	}

	public int getAttrib(String name) {
		return GL20.glGetAttribLocation(id, name);
	}

    public int getUniform(String name) {
        return GL20.glGetUniformLocation(id, name);
    }

	public void setUniform(String name, int value) {
	    GL20.glUniform1i(GL20.glGetUniformLocation(id, name), value);
	}

}
