package game;

import game.events.EventBus;

import java.io.IOException;
import java.net.URL;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;

public class GLShader extends GLResource {
	
	private static final Logger log = LoggerFactory.getLogger(GLShader.class);

	int id;

	public GLShader(EventBus eventBus, int type) {
	  super(eventBus);
		id = GL20.glCreateShader(type);
	}

	public GLShader compile(String shaderProgramResource) {
		URL url = Resources.getResource(shaderProgramResource);
		try {
			String shaderProgramString = Resources.toString(url, Charsets.UTF_8);
			log.info("Shader program:\n" + shaderProgramString);
			GL20.glShaderSource(id, shaderProgramString);
			GL20.glCompileShader(id);
			if (GL20.glGetShader(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
				throw new RuntimeException(
						"Unable to compile shader: " + shaderProgramResource + ": "
								+ GL20.glGetShaderInfoLog(id, 4 * 1024));
			}
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return this;
	}

	protected Runnable dispose() {
		return () -> GL20.glDeleteShader(id);
	}

	public int getId() {
		return id;
	}

}
