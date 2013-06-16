package game.shaders;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import game.Context;
import game.Renderable;
import game.math.Matrix;
import game.math.Vector;

import java.io.File;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Charsets;
import com.google.common.base.Throwables;
import com.google.common.io.Files;

public class Box implements Renderable {

  Context context;
  int     program = 0;
  FloatBuffer     tr = BufferUtils.createFloatBuffer(16);

  public Box(Context context) {
    this.context = context;
    
    int vertShader = 0, fragShader = 0;
    Matrix.rot(Math.PI/2, Vector.UP).writeToBuffer(tr);

    try {
      vertShader = createShader("shaders/screen.vert", ARBVertexShader.GL_VERTEX_SHADER_ARB);
      fragShader = createShader("shaders/screen.frag", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
      if (vertShader == 0 || fragShader == 0) {
        throw new RuntimeException("Unable to load shaders");
      }
    } catch (Exception e) {
      Throwables.propagate(e);
    }

    program = ARBShaderObjects.glCreateProgramObjectARB();

    if (program == 0) {
      throw new RuntimeException("Unable to create program");
    }

    ARBShaderObjects.glAttachObjectARB(program, vertShader);
    ARBShaderObjects.glAttachObjectARB(program, fragShader);
    ARBShaderObjects.glLinkProgramARB(program);

    if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
      throw new RuntimeException("Unable to link program: " + getLogInfo(program));
    }

    ARBShaderObjects.glValidateProgramARB(program);
    if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
      throw new RuntimeException("Unable to validate program: " + getLogInfo(program));
    }
  }

  public void render() {
    ARBShaderObjects.glUseProgramObjectARB(program);
    int trId = ARBShaderObjects.glGetUniformLocationARB(program, "tr");
    if ( trId != -1 ) {
      ARBShaderObjects.glUniformMatrix4ARB(trId, true, tr);
    } else {
      throw new RuntimeException("unable to obtain variable location");
    }
    
    if ( false ) {
    
      GL11.glPushMatrix();
      GL11.glLoadIdentity();
      GL11.glTranslatef(0.0f, 0.0f, -10.0f);
      GL11.glBegin(GL11.GL_QUADS);
      GL11.glColor3f(1.0f, 1.0f, 1.0f);
      GL11.glVertex3f(-1.0f, 1.0f, 0.0f);
      GL11.glVertex3f(1.0f, 1.0f, 0.0f);
      GL11.glVertex3f(1.0f, -1.0f, 0.0f);
      GL11.glVertex3f(-1.0f, -1.0f, 0.0f);
      GL11.glEnd();
      GL11.glPopMatrix();
      
    }

    ARBShaderObjects.glUseProgramObjectARB(0);
  }

  private int createShader(String filename, int shaderType) {
    int shader = 0;
    shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);

    if (shader == 0) {
      throw new RuntimeException("Unable to load shaders");
    }

    try {
      ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
      ARBShaderObjects.glCompileShaderARB(shader);

      if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE) {
        throw new RuntimeException("Unable to create shader: " + getLogInfo(shader));
      }
    } catch (Exception e) {
      ARBShaderObjects.glDeleteObjectARB(shader);
      Throwables.propagate(e);
    }
    return shader;
  }

  private static String getLogInfo(int obj) {
    return ARBShaderObjects.glGetInfoLogARB(obj,
      ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
  }

  private String readFileAsString(String filename) {
    try {
      return Files.toString(new File(filename), Charsets.UTF_8);
    } catch(Exception e) {
      Throwables.propagate(e);
      return null;
    }
  }
}

