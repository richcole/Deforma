package game.models;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glVertex3d;

import org.lwjgl.opengl.GL11;

import game.Renderable;
import game.math.Vector;

public class Rect implements Renderable {
  
  Vector pos;
  Vector normal;
  Vector up;
  Vector left;
  
  Rect(Vector pos, Vector up, Vector left) {
    this.pos = pos;
    this.up = up;
    this.left = left;
    this.normal = left.cross(up).scaleTo(1.0);
  }

  @Override
  public void render() {
    Vector[] corners = {
      pos.plus(up).plus(left), pos.plus(up).minus(left), pos.minus(up).minus(left), pos.minus(up).plus(left)   
    };
    GL11.glColor3d(1.0f, 1.0f, 1.0f);
    GL11.glBegin(GL_QUADS);
    for(Vector v: corners) {
      GL11.glNormal3d(normal.x(), normal.y(), normal.z());
      GL11.glVertex3d(v.x(), v.y(), v.z());
    }
    GL11.glEnd();
    
  }

}
