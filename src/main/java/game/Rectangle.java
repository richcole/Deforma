package game;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3d;


public class Rectangle implements Renderable {
  
  Context context;
  
  Vector size;
  Vector position;
 
  public Rectangle(Context context, Vector position, Vector size) {
    this.size = new Vector(size);
    this.position = new Vector(position);
    this.context = context;
    context.getScene().register(this);
  }
  
  public void render() {
    context.getStoneTexture().bind();

    glBegin(GL_QUADS);  
    glColor3d(1.0f, 1.0f, 1.0f);

    // y constant
    glTexCoord2f(0, 0);
    glVertex3d(position.x() - size.x(), position.y() - size.y(), position.z() - size.z());

    glTexCoord2f(1, 0);
    glVertex3d(position.x() + size.x(), position.y() - size.y(), position.z() - size.z());

    glTexCoord2f(1, 1);
    glVertex3d(position.x() + size.x(), position.y() - size.y(), position.z() + size.z());

    glTexCoord2f(0, 1);
    glVertex3d(position.x() - size.x(), position.y() - size.y(), position.z() + size.z());
    glEnd();

    glBegin(GL_QUADS);  
    glColor3d(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3d(position.x() - size.x(), position.y() + size.y(), position.z() - size.z());

    glTexCoord2f(1, 0);
    glVertex3d(position.x() + size.x(), position.y() + size.y(), position.z() - size.z());

    glTexCoord2f(1, 1);
    glVertex3d(position.x() + size.x(), position.y() + size.y(), position.z() + size.z());

    glTexCoord2f(0, 1);
    glVertex3d(position.x() - size.x(), position.y() + size.y(), position.z() + size.z());
    glEnd();

    // x constant
    glBegin(GL_QUADS);  
    glColor3d(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3d(position.x() - size.x(), position.y() - size.y(), position.z() - size.z());

    glTexCoord2f(1, 0);
    glVertex3d(position.x() - size.x(), position.y() - size.y(), position.z() + size.z());

    glTexCoord2f(1, 1);
    glVertex3d(position.x() - size.x(), position.y() + size.y(), position.z() + size.z());

    glTexCoord2f(0, 1);
    glVertex3d(position.x() - size.x(), position.y() + size.y(), position.z() - size.z());
    glEnd();

    glBegin(GL_QUADS);  
    glColor3d(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3d(position.x() + size.x(), position.y() - size.y(), position.z() - size.z());

    glTexCoord2f(1, 0);
    glVertex3d(position.x() + size.x(), position.y() - size.y(), position.z() + size.z());

    glTexCoord2f(1, 1);
    glVertex3d(position.x() + size.x(), position.y() + size.y(), position.z() + size.z());

    glTexCoord2f(0, 1);
    glVertex3d(position.x() + size.x(), position.y() + size.y(), position.z() - size.z());
    glEnd();

    // z constant
    glBegin(GL_QUADS);  
    glColor3d(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3d(position.x() - size.x(), position.y() - size.y(), position.z() - size.z());

    glTexCoord2f(1, 0);
    glVertex3d(position.x() - size.x(), position.y() + size.y(), position.z() - size.z());

    glTexCoord2f(1, 1);
    glVertex3d(position.x() + size.x(), position.y() + size.y(), position.z() - size.z());

    glTexCoord2f(0, 1);
    glVertex3d(position.x() + size.x(), position.y() - size.y(), position.z() - size.z());
    glEnd();

    glBegin(GL_QUADS);  
    glColor3d(1.0f, 1.0f, 1.0f);

    glTexCoord2f(0, 0);
    glVertex3d(position.x() - size.x(), position.y() - size.y(), position.z() + size.z());

    glTexCoord2f(1, 0);
    glVertex3d(position.x() - size.x(), position.y() + size.y(), position.z() + size.z());

    glTexCoord2f(1, 1);
    glVertex3d(position.x() + size.x(), position.y() + size.y(), position.z() + size.z());

    glTexCoord2f(0, 1);
    glVertex3d(position.x() + size.x(), position.y() - size.y(), position.z() + size.z());
    glEnd();
  }

  public void setPosition(Vector position) {
    this.position = new Vector(position);
  }
  
  public void setSize(Vector size) {
    this.size = new Vector(size);
  }

  public Vector getPosition() {
    return position;
  }

  public void move(Vector velocity) {
    position = position.plus(velocity);
  }
}
