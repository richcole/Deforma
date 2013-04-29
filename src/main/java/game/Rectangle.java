package game;

import java.io.File;

import javax.vecmath.Vector3f;

import org.apache.log4j.Logger;

import static org.lwjgl.opengl.GL11.*;

public class Rectangle implements HasRender {
  
  private static Logger logger = Logger.getLogger(Rectangle.class);

  Vector3f d;
  Vector3f pos;
  Texture texture;
  
  public void init() {
    d = new Vector3f(200, 200, 200);
    pos = new Vector3f(100, -50, -100);
  }
  
  public void initGL() {
    texture = new Texture(new File("res/image.jpg"));
  }
  
  public void render() {
    texture.bind();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    // y constant
    glTexCoord2f(0, 0);
    glVertex3f(pos.x - d.x, pos.y - d.y, pos.z - d.z);

    glTexCoord2f(1, 0);
    glVertex3f(pos.x + d.x, pos.y - d.y, pos.z - d.z);

    glTexCoord2f(1, 1);
    glVertex3f(pos.x + d.x, pos.y - d.y, pos.z + d.z);

    glTexCoord2f(0, 1);
    glVertex3f(pos.x - d.x, pos.y - d.y, pos.z + d.z);
    glEnd();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(pos.x - d.x, pos.y + d.y, pos.z - d.z);

    glTexCoord2f(1, 0);
    glVertex3f(pos.x + d.x, pos.y + d.y, pos.z - d.z);

    glTexCoord2f(1, 1);
    glVertex3f(pos.x + d.x, pos.y + d.y, pos.z + d.z);

    glTexCoord2f(0, 1);
    glVertex3f(pos.x - d.x, pos.y + d.y, pos.z + d.z);
    glEnd();

    // x constant
    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(pos.x - d.x, pos.y - d.y, pos.z - d.z);

    glTexCoord2f(1, 0);
    glVertex3f(pos.x - d.x, pos.y - d.y, pos.z + d.z);

    glTexCoord2f(1, 1);
    glVertex3f(pos.x - d.x, pos.y + d.y, pos.z + d.z);

    glTexCoord2f(0, 1);
    glVertex3f(pos.x - d.x, pos.y + d.y, pos.z - d.z);
    glEnd();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(pos.x + d.x, pos.y - d.y, pos.z - d.z);

    glTexCoord2f(1, 0);
    glVertex3f(pos.x + d.x, pos.y - d.y, pos.z + d.z);

    glTexCoord2f(1, 1);
    glVertex3f(pos.x + d.x, pos.y + d.y, pos.z + d.z);

    glTexCoord2f(0, 1);
    glVertex3f(pos.x + d.x, pos.y + d.y, pos.z - d.z);
    glEnd();

    // z constant
    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(pos.x - d.x, pos.y - d.y, pos.z - d.z);

    glTexCoord2f(1, 0);
    glVertex3f(pos.x - d.x, pos.y + d.y, pos.z - d.z);

    glTexCoord2f(1, 1);
    glVertex3f(pos.x + d.x, pos.y + d.y, pos.z - d.z);

    glTexCoord2f(0, 1);
    glVertex3f(pos.x + d.x, pos.y - d.y, pos.z - d.z);
    glEnd();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(pos.x - d.x, pos.y - d.y, pos.z + d.z);

    glTexCoord2f(1, 0);
    glVertex3f(pos.x - d.x, pos.y + d.y, pos.z + d.z);

    glTexCoord2f(1, 1);
    glVertex3f(pos.x + d.x, pos.y + d.y, pos.z + d.z);

    glTexCoord2f(0, 1);
    glVertex3f(pos.x + d.x, pos.y - d.y, pos.z + d.z);
    glEnd();
  }
  
}
