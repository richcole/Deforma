package game.voxel;

import java.io.IOException;
import java.net.URL;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Resources;

public class GLShader {

    int id;
    
    public GLShader(int type) {
    	id = GL20.glCreateShader(type);
    }
    
    public GLShader compile(String shaderProgramResource) {    	
    	URL url = Resources.getResource(shaderProgramResource);
    	try {
    		String shaderProgramString = Resources.toString(url, Charsets.UTF_8);
        	GL20.glShaderSource(id, shaderProgramString);
            GL20.glCompileShader(id);
            if (GL20.glGetShader(id, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
              throw new RuntimeException("Unable to compile shader: " + shaderProgramResource + ": " + GL20.glGetShaderInfoLog(id, 4*1024));
            }
    	} catch(IOException e) {
    		Throwables.propagate(e);
    	}
    	return this;
    }
    
    void finalizer() {
    	GL20.glDeleteShader(id);
    }

	public int getId() {
		return id;
	}

}