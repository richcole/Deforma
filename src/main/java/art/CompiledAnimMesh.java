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

public class CompiledAnimMesh implements GLResource, Renderable {

	GLVertexArray vao;
	GLBuffer vbo, tbo, ibo, bbo;

	private AnimProgram program;
	private TextureSupplier tex;
	private int vert, texCoords, boneIndex;
	private Mesh mesh;
    
    public CompiledAnimMesh(AnimProgram program, TextureSupplier tex, Mesh mesh) {
    	this.program = program;
    	this.tex = tex;
    	this.mesh = mesh;
    }
	
	public void init() {
		vert = program.getVert();
		texCoords = program.getTexCoords();
		
		Preconditions.checkArgument(vert >= 0);
		Preconditions.checkArgument(texCoords >= 0);

		vao = new GLVertexArray();

		vbo = new GLBuffer();
		vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, getVertexData());

		tbo = new GLBuffer();
		vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, getTexCoordData());
		
		bbo = new GLBuffer();
		vao.bindData(boneIndex, GL15.GL_ARRAY_BUFFER, bbo, 2, getBoneIndexData());

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

	private FloatBuffer getBoneIndexData() {
		List<Integer> bones = mesh.getBones();
		FloatBuffer buf = BufferUtils.createFloatBuffer(bones.size());
		
		for(Integer bone: bones) {
			buf.put((float)bone);
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
		program.use();
		tex.getTexture().bind(program.getTex());
		
		GL30.glBindVertexArray(vao.getId());
		GL20.glEnableVertexAttribArray(vert);
		GL20.glEnableVertexAttribArray(texCoords);
		
		ibo.drawElements();
	}
	
}
