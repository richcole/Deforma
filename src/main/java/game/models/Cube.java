package game.models;

import game.Context;
import game.Material;
import game.Renderable;
import game.base.Texture;
import game.math.Vector;

import java.util.List;

import com.google.common.collect.Lists;


public class Cube implements Renderable {
  
  Context context;
  
  Vector up;
  Vector left;
  Vector pos;
  Vector normal;
  Material material;
  List<Rect> rects;
  List<Texture> textures;
   
  public Cube(Context context, Vector pos, double size) {
    this(context, pos, Vector.LEFT.scaleTo(size), Vector.UP.scaleTo(size), Vector.NORMAL.scaleTo(size));
  }
  
  public Cube(Context context, Vector pos, Vector left, Vector up, Vector normal) {
    this.context = context;
    this.pos = pos;
    this.left = left;
    this.up = up;
    this.normal = normal;
    this.material = context.getMaterial();
    this.textures = getTextures();
    this.rects = Lists.newArrayList(
      new Rect(pos.plus(normal),  up,      left,           getTexture(0)), // front
      new Rect(pos.minus(normal), up,      left.minus(),   getTexture(1)), // back
      new Rect(pos.plus(left),    up,      normal.minus(), getTexture(3)), // left
      new Rect(pos.minus(left),   up,      normal,         getTexture(2)), // right
      new Rect(pos.plus(up),      left.minus(),  normal.minus(),           getTexture(4)), // top
      new Rect(pos.minus(up),     left,    normal.minus(), getTexture(5))  // bottom
    );
  }

  private Texture getTexture(int i) {
    return textures.get(i % textures.size());
  }
  
  protected List<Texture> getTextures() {
    return Lists.newArrayList(context.getStoneTexture());
  }

  public void render() {
    material.render();
    for(Rect rect: rects) {
      rect.render();
    }
  }

  public void setPos(Vector pos) {
    this.pos = pos;
  }
}
