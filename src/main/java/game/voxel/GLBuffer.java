package game.voxel;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;

public class GLBuffer {
	
	int id;
	int numIndexes;
	
	public GLBuffer() {
		id = GL15.glGenBuffers();
	}
	
	public void finalize() {
		GL15.glDeleteBuffers(id);
	}

	public int getId() {
		return id;
	}

	public GLBuffer bindData(int target, FloatBuffer buffer) {
		GL15.glBindBuffer(target, id);
		GL15.glBufferData(target, buffer, GL15.GL_STATIC_DRAW);
		return this;
	}

	public GLBuffer bindData(int target, IntBuffer buffer) {
		numIndexes = buffer.limit() - buffer.position();
		GL15.glBindBuffer(target, id);
		GL15.glBufferData(target, buffer, GL15.GL_STATIC_DRAW);
		return this;
	}

	public void drawElements() {
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, id);
	    GL11.glDrawElements(GL11.GL_TRIANGLES, numIndexes, GL11.GL_UNSIGNED_INT, 0);
	}

	public int getElementCount() {
		return numIndexes;
	}

	
}
