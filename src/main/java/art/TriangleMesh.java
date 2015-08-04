package art;

import org.lwjgl.opengl.GL15;

import com.google.common.base.Preconditions;

import game.Model;
import game.Utils;
import game.gl.GLBuffer;
import game.gl.GLResource;
import game.gl.GLTexture;
import game.gl.GLVertexArray;
import game.math.Matrix;

public class TriangleMesh extends GLResource implements Model {

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
	private TextureSupplier texSupplier;

	private int vert;
	private int texCoords;
	private GLTexture tex;
	private Matrix modelTr;
    
    public TriangleMesh(SimpleProgram simpleProgram, TextureSupplier tex) {
    	this.simpleProgram = simpleProgram;
    	this.texSupplier = tex;
    	this.modelTr = Matrix.IDENTITY;
    }
	
	public void init() { 
		simpleProgram.ensureInitialized();
	
		vert = simpleProgram.getVert();
		texCoords = simpleProgram.getTexCoords();
		Preconditions.checkArgument(vert >= 0);
		Preconditions.checkArgument(texCoords >= 0);
		vao = new GLVertexArray();

		vbo = new GLBuffer();
		vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, Utils.toFloatBuffer(verticesData));

		tbo = new GLBuffer();
		vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, Utils.toFloatBuffer(verticesData));
		
		this.tex = texSupplier.getTexture();
	}

	public void dispose() {
	}

	public void render() {
		simpleProgram.use();
		tex.bind(simpleProgram.getTex());
		simpleProgram.setModelTr(modelTr);
		vao.drawArrays();
	}

	@Override
	public void setModelTr(Matrix modelTr) {
		this.modelTr = modelTr;
	}
	
	
}
