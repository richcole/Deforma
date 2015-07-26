package art;

import org.lwjgl.opengl.GL20;

import game.gl.GLProgram;
import game.gl.GLResource;
import game.gl.GLShader;

public class SimpleProgram implements GLResource {

	private GLProgram program;
    private int vert, tr, texCoords;

	public void init() {
		program = new GLProgram();
		program.attach(new GLShader(GL20.GL_VERTEX_SHADER).compile("simple.vert"));
		program.attach(new GLShader(GL20.GL_FRAGMENT_SHADER).compile("simple.frag"));
		program.link();
		program.setUniform("tex", 0);
        vert = program.getAttrib("vert");
        texCoords = program.getAttrib("texCoords");
        tr = program.getUniform("tr");
	}

	public void dispose() {
	}
	
	public int getVert() {
		return vert;
	}

    public int getTr() {
        return tr;
    }

	public void use() {
		program.use();
	}

	public int getTexCoords() {
		return texCoords;
	}

	public int getTex() {
		return 0;
	}
}
