package game.voxel;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;

public class CompiledMesh extends GLResource implements Model  {

	private SimpleProgram simpleProgram;
	private GLTexture tex;

	private GLVertexArray vao;
	private GLBuffer vbo, tbo, ibo;

	private int vert;
	private int texCoords;
	
	private Mesh mesh;
	private Matrix modelTr;
	
	private boolean wireFrame = false;

	
    public CompiledMesh(SimpleProgram simpleProgram, Mesh mesh) {
    	this.simpleProgram = simpleProgram;
    	this.mesh = mesh;
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
		vao.bindData(vert, GL15.GL_ARRAY_BUFFER, vbo, 3, getVertexData());

		tbo = new GLBuffer();
		vao.bindData(texCoords, GL15.GL_ARRAY_BUFFER, tbo, 2, getTexCoordData());
		
		ibo = new GLBuffer();
		ibo.bindData(GL15.GL_ELEMENT_ARRAY_BUFFER, getElementData());
		
		tex = mesh.getMaterial().getTexture();
		Preconditions.checkNotNull(tex);
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
		tex.bind(simpleProgram.getTex());
		simpleProgram.setModelTr(modelTr);
		
		GL30.glBindVertexArray(vao.getId());
		GL20.glEnableVertexAttribArray(vert);
		GL20.glEnableVertexAttribArray(texCoords);

		if ( wireFrame ) {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_LINE);
		} else {
			GL11.glPolygonMode(GL11.GL_FRONT_AND_BACK, GL11.GL_FILL);
		}
		ibo.drawElements();
	}

	@Override
	public void setModelTr(Matrix modelTr) {
		this.modelTr = modelTr;
	}

	public void setWireFrame(boolean wireFrame) {
		this.wireFrame = wireFrame;
	}
	
}