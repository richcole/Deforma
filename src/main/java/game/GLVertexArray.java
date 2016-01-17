package game;

import game.events.EventBus;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class GLVertexArray extends GLResource {

	int id;
	int vertexCount;
	int elementCount; 
	
	public GLVertexArray(EventBus eventBus) {
	  super(eventBus);
		id = GL30.glGenVertexArrays();
	}

	protected Runnable dispose() {
	  return () -> GL30.glDeleteVertexArrays(id);
	}

	public void bindData(int position, int bufferType, GLBuffer buffer, int valuesPerVertex, FloatBuffer data) {
		vertexCount = (data.limit() - data.position()) / valuesPerVertex;
		GL30.glBindVertexArray(id);
		GL15.glBindBuffer(bufferType, buffer.id);
		GL15.glBufferData(bufferType, data, GL15.GL_STATIC_DRAW);
		GL20.glEnableVertexAttribArray(position);
		GL20.glVertexAttribPointer(position, valuesPerVertex, GL11.GL_FLOAT, false, 0, 0);

		GL15.glBindBuffer(bufferType, 0);
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(position);
	}
	
	public void bindData(int position, int bufferType, GLBuffer buffer, IntBuffer data) {
		elementCount = (data.limit() - data.position());
		GL30.glBindVertexArray(id);
		GL15.glBindBuffer(bufferType, buffer.id);
		GL15.glBufferData(bufferType, data, GL15.GL_STATIC_DRAW);

		GL15.glBindBuffer(bufferType, 0);
		GL30.glBindVertexArray(0);
		GL20.glDisableVertexAttribArray(position);
	}

	public void drawArrays() {
		GL30.glBindVertexArray(id);
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vertexCount);
	}

	public int getId() {
		return id;
	}

}
