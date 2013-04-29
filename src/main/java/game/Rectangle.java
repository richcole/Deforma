package game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3f;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3f;

public class Rectangle implements Renderable {
  
  Context context;
  
  private Vector3f size;
  private Vector3f position;
 
  public Rectangle(Context context, Vector3f position, Vector3f size) {
    this.size = new Vector3f(size);
    this.position = new Vector3f(position);
    this.context = context;
    context.getScene().register(this);
  }
  
  public void render() {
    context.getStoneTexture().bind();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    // y constant
    glTexCoord2f(0, 0);
    glVertex3f(position.x - size.x, position.y - size.y, position.z - size.z);

    glTexCoord2f(1, 0);
    glVertex3f(position.x + size.x, position.y - size.y, position.z - size.z);

    glTexCoord2f(1, 1);
    glVertex3f(position.x + size.x, position.y - size.y, position.z + size.z);

    glTexCoord2f(0, 1);
    glVertex3f(position.x - size.x, position.y - size.y, position.z + size.z);
    glEnd();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(position.x - size.x, position.y + size.y, position.z - size.z);

    glTexCoord2f(1, 0);
    glVertex3f(position.x + size.x, position.y + size.y, position.z - size.z);

    glTexCoord2f(1, 1);
    glVertex3f(position.x + size.x, position.y + size.y, position.z + size.z);

    glTexCoord2f(0, 1);
    glVertex3f(position.x - size.x, position.y + size.y, position.z + size.z);
    glEnd();

    // x constant
    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(position.x - size.x, position.y - size.y, position.z - size.z);

    glTexCoord2f(1, 0);
    glVertex3f(position.x - size.x, position.y - size.y, position.z + size.z);

    glTexCoord2f(1, 1);
    glVertex3f(position.x - size.x, position.y + size.y, position.z + size.z);

    glTexCoord2f(0, 1);
    glVertex3f(position.x - size.x, position.y + size.y, position.z - size.z);
    glEnd();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(position.x + size.x, position.y - size.y, position.z - size.z);

    glTexCoord2f(1, 0);
    glVertex3f(position.x + size.x, position.y - size.y, position.z + size.z);

    glTexCoord2f(1, 1);
    glVertex3f(position.x + size.x, position.y + size.y, position.z + size.z);

    glTexCoord2f(0, 1);
    glVertex3f(position.x + size.x, position.y + size.y, position.z - size.z);
    glEnd();

    // z constant
    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(position.x - size.x, position.y - size.y, position.z - size.z);

    glTexCoord2f(1, 0);
    glVertex3f(position.x - size.x, position.y + size.y, position.z - size.z);

    glTexCoord2f(1, 1);
    glVertex3f(position.x + size.x, position.y + size.y, position.z - size.z);

    glTexCoord2f(0, 1);
    glVertex3f(position.x + size.x, position.y - size.y, position.z - size.z);
    glEnd();

    glBegin(GL_QUADS);  
    glColor3f(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3f(position.x - size.x, position.y - size.y, position.z + size.z);

    glTexCoord2f(1, 0);
    glVertex3f(position.x - size.x, position.y + size.y, position.z + size.z);

    glTexCoord2f(1, 1);
    glVertex3f(position.x + size.x, position.y + size.y, position.z + size.z);

    glTexCoord2f(0, 1);
    glVertex3f(position.x + size.x, position.y - size.y, position.z + size.z);
    glEnd();
  }

  public void setPosition(Vector3f position) {
    this.position = new Vector3f(position);
  }
  
  public void setSize(Vector3f size) {
    this.size = new Vector3f(size);
  }

  public Vector3f getPosition() {
    return position;
  }
}
