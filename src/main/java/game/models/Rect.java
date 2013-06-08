package game.models;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import game.Renderable;
import game.base.Texture;
import game.math.Vector;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.google.common.collect.Lists;

public class Rect implements Renderable {
  
  Vector pos;
  Vector normal;
  Vector up;
  Vector left;
  Texture texture;
  List<Vector> textureCoords;
  
  Rect(Vector pos, Vector up, Vector left, Texture texture) {
    this.pos = pos;
    this.up = up;
    this.left = left;
    this.normal = left.cross(up).scaleTo(1.0);
    this.texture = texture;
    this.textureCoords = Lists.newArrayList(
      new Vector(0,    0,   0.0, 1.0),  
      new Vector(1.0,  0,   0.0, 1.0),  
      new Vector(1.0,  1.0, 0.0, 1.0),  
      new Vector(0,    1.0, 0.0, 1.0)  
    );
  }

  @Override
  public void render() {
    Vector[] corners = {
      pos.plus(up).plus(left), pos.plus(up).minus(left), pos.minus(up).minus(left), pos.minus(up).plus(left)   
    };
    GL11.glColor3d(1.0f, 1.0f, 1.0f);
    texture.bind();
    GL11.glBegin(GL_QUADS);
    for(int i=0;i<4;++i) {
      Vector v = corners[i];
      Vector t = textureCoords.get(i);
      GL11.glTexCoord2d(t.x(), t.y());
      GL11.glNormal3d(normal.x(), normal.y(), normal.z());
      GL11.glVertex3d(v.x(), v.y(), v.z());
    }
    GL11.glEnd();
    
  }

  public void setPos(Vector pos) {
    this.pos = pos;
  }

}
