package art;

import org.lwjgl.opengl.GL15;

import com.google.common.base.Preconditions;

import game.Renderable;
import game.Util;
import game.gl.GLBuffer;
import game.gl.GLResource;
import game.gl.GLVertexArray;

public class TriangleMesh implements GLResource, Renderable {

	GLVertexArray vao;
	GLBuffer vbo, tbo;
	
    static float[] verticesData = { 
      0.0f,  0.8f,  0.0f,
	  -0.8f, -0.8f, 0.0f,
	  0.8f,  -0.8f, 0.0f 
	};
    
    static float[] texCoordsData = {
       0.5f, 1.0f,
	   0.0f, 0.0f,
	   1.0f, 0.0f,    		
    };
    
	private SimpleProgram simpleProgram;
	private TextureSupplier tex;
	private int vert;
	private int texCoords;
    
    public TriangleMesh(SimpleProgram simpleProgram, TextureSupplier tex) {
    	this.simpleProgram = simpleProgram;
    	this.tex = tex;
    }
	
	public void init() {
		vert = simpleProgram.getVert();
		texCoords = simpleProgram.getTexCoords();
		Preconditions.checkArgument(vert >= 0);
		Preconditions.checkArgument(texCoords >= 0);
		vao = new GLVertexArray();

		vbo = new GLBuffer();
		vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, Util.toFloatBuffer(verticesData));

		tbo = new GLBuffer();
		vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, Util.toFloatBuffer(verticesData));
	}

	public void dispose() {
	}

	public void render() {
		simpleProgram.use();
		tex.getTexture().bind(simpleProgram.getTex());
		vao.drawArrays();
	}
	
	
}
