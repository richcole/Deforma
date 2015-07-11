package art;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;

import game.Renderable;
import game.gl.GLBuffer;
import game.gl.GLResource;
import game.gl.GLVertexArray;
import game.math.Vector;

public class CompiledMesh implements GLResource, Renderable {

	GLVertexArray vao;
	GLBuffer vbo, tbo, ibo;

	private SimpleProgram simpleProgram;
	private TextureSupplier tex;
	private int vert;
	private int texCoords;
	private Mesh mesh;
    
    public CompiledMesh(SimpleProgram simpleProgram, TextureSupplier tex, Mesh mesh) {
    	this.simpleProgram = simpleProgram;
    	this.tex = tex;
    	this.mesh = mesh;
    }
	
	public void init() {
		vert = simpleProgram.getVert();
		texCoords = simpleProgram.getTexCoords();
		Preconditions.checkArgument(vert >= 0);
		Preconditions.checkArgument(texCoords >= 0);

		vao = new GLVertexArray();

		vbo = new GLBuffer();
		vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, getVertexData());

		tbo = new GLBuffer();
		vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, getTexCoordData());
		
		ibo = new GLBuffer();
		ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, getElementData());
	}

	private FloatBuffer getVertexData() {
		List<Vector> vertices = mesh.getVertices();
		FloatBuffer buf = BufferUtils.createFloatBuffer(vertices.size()*3);
		
		for(Vector vector: vertices) {
			buf.put((float)vector.x());
			buf.put((float)vector.y());
			buf.put((float)vector.z());
		}
		buf.flip();
		
		return buf;
	}

	private FloatBuffer getTexCoordData() {
		List<Vector> vertices = mesh.getTexCoords();
		FloatBuffer buf = BufferUtils.createFloatBuffer(vertices.size()*2);
		
		for(Vector vector: vertices) {
			buf.put((float)vector.x());
			buf.put((float)vector.y());
		}
		buf.flip();
		
		return buf;
	}

	private IntBuffer getElementData() {
		List<Integer> indexes = mesh.getElements();
		IntBuffer buf = BufferUtils.createIntBuffer(indexes.size());
		
		for(Integer index: indexes) {
			buf.put(index);
		}
		buf.flip();
		
		return buf;
	}

	public void dispose() {
	}

	public void render() {
		simpleProgram.use();
		tex.getTexture().bind(simpleProgram.getTex());
		
		GL30.glBindVertexArray(vao.getId());
		GL20.glEnableVertexAttribArray(vert);
		GL20.glEnableVertexAttribArray(texCoords);
		
		ibo.drawElements();
	}
	
}
