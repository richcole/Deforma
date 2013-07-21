package game.proc;

import game.Context;
import game.Renderable;
import game.base.textures.TextureTile;
import game.math.Vector;
import game.utils.GLUtils;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class HeightMap implements Renderable {
  
  static private Logger logger = Logger.getLogger(HeightMap.class); 
  
  int width;
  int depth;
  Vector normals[];
  Vector pos[];

  private Context context;
  
  public HeightMap(Context context, int width, int depth) {
    this.context = context;
    this.width = width;
    this.depth = depth;
    this.pos = new Vector[width*depth];
    this.normals = new Vector[width*depth];
  }
  
  public void set(int x, int y, double h) {
    int i = getIndex(x,y);
    pos[i] = new Vector(x, y, h, 1);
  }

  public void setIfNotSet(int x, int y, double h) {
    int i = getIndex(x,y);
    if ( pos[i] == null ) {
      pos[i] = new Vector(x, y, h, 1);
    }
  }

  public int getIndex(int x, int y) {
    if ( x >= width || y >= depth ) {
      throw new RuntimeException("Invalid coords x=" + x + " y=" + y);
    }
    return (y*width) + x;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getDepth() {
    return depth;
  }

  public void setDepth(int depth) {
    this.depth = depth;
  }

  public double get(int x, int y) {
    int i = getIndex(x, y);
    if ( pos[i] != null ) {
      return pos[i].z();
    } else {
      return 0.0;
    }
  }
  
  public void calculateNormals() {
    for(int x=0;x<width; ++x) {
      normals[getIndex(x, 0)] = new Vector(0, 0, 1, 1);
      normals[getIndex(x, depth-1)] = new Vector(0, 0, 1, 1);
    }
    for(int y=0;y<depth; ++y) {
      normals[getIndex(0, y)] = new Vector(0, 0, 1, 1);
      normals[getIndex(width-1, y)] = new Vector(0, 0, 1, 1);
    }
    for(int x=1;x<width-1; ++x) {
      for(int y=1;y<depth-1; ++y) {
        Vector a = new Vector(1, 0, get(x+1, y)-get(x+1, y), 1).normalize();
        Vector b = new Vector(0, 1, get(x, y+1)-get(x, y-1), 1).normalize();
        normals[getIndex(x,y)] = a.cross(b);
      }
    }
  }

  @Override
  public void render() {
    TextureTile texture = context.getStoneTexture();
    texture.bind();
    double scale = 10;
    
    GL11.glBegin(GL11.GL_TRIANGLES);
    double w = width;
    double d = depth;
    for(int x=1;x<width-1; ++x) {
      for(int y=1;y<depth-1; ++y) {
        // top left
        GL11.glTexCoord3d((x-1)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x-1, y).times(scale));
        GLUtils.glNormal(getNormal(x-1, y));

        GL11.glTexCoord3d((x)/w, (y-1)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y-1).times(scale));
        GLUtils.glNormal(getNormal(x, y-1));

        GL11.glTexCoord3d((x)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y).times(scale));
        GLUtils.glNormal(getNormal(x, y));

        // top right
        GL11.glTexCoord3d((x)/w, (y-1)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y-1).times(scale));
        GLUtils.glNormal(getNormal(x, y-1));

        GL11.glTexCoord3d((x+1)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x+1, y).times(scale));
        GLUtils.glNormal(getNormal(x+1, y));

        GL11.glTexCoord3d((x)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y).times(scale));
        GLUtils.glNormal(getNormal(x, y));

        // bottom left
        GL11.glTexCoord3d((x-1)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x-1, y).times(scale));
        GLUtils.glNormal(getNormal(x-1, y));

        GL11.glTexCoord3d((x)/w, (y+1)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y+1).times(scale));
        GLUtils.glNormal(getNormal(x, y+1));

        GL11.glTexCoord3d((x)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y).times(scale));
        GLUtils.glNormal(getNormal(x, y));

        // bottom right
        GL11.glTexCoord3d((x+1)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x+1, y).times(scale));
        GLUtils.glNormal(getNormal(x+1, y));

        GL11.glTexCoord3d((x)/w, (y+1)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y+1).times(scale));
        GLUtils.glNormal(getNormal(x, y+1));

        GL11.glTexCoord3d((x)/w, (y)/d, texture.getTextureZ());
        GLUtils.glVertex(getPosition(x, y).times(scale));
        GLUtils.glNormal(getNormal(x, y));
      }
    }
    GL11.glEnd();
  }

  private Vector getPosition(int x, int y) {
    return pos[getIndex(x,y)];
  }

  private Vector getNormal(int x, int y) {
    return normals[getIndex(x, y)];
  }

  public void register() {
    context.getScene().register(this);
  }
}
