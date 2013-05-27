package game;

import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_EMISSION;
import static org.lwjgl.opengl.GL11.GL_SHININESS;
import static org.lwjgl.opengl.GL11.glMaterial;
import static org.lwjgl.opengl.GL11.glMaterialf;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;


public class Material implements Renderable {
  
  private FloatBuffer whiteLight;
  private FloatBuffer dimLight;

  public Material() {
    whiteLight = BufferUtils.createFloatBuffer(4);
    whiteLight.put(1f).put(1.0f).put(1.0f).put(1.0f).flip();
    dimLight = BufferUtils.createFloatBuffer(4);
    dimLight.put(0.2f).put(0.2f).put(0.2f).put(1.0f).flip();
  }
  
  public void render() {
    glMaterial(GL_FRONT_AND_BACK,  GL_EMISSION,  dimLight);
    glMaterialf(GL_FRONT_AND_BACK, GL_SHININESS, 50.0f);          
  }
}
