package game.models;

import game.Context;
import game.base.Texture;
import game.math.Vector;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

public class SkyBox extends Cube {
  
  final static Vector SIZE = Vector.ONES.times(10000);
  
  List<Texture> textures;

  public SkyBox(Context context) {
    super(context, Vector.ZERO, SIZE);
    textures = Lists.newArrayList();
    textures.add(loadTexture("res/left.jpg"));
    textures.add(loadTexture("res/right.jpg"));
    textures.add(loadTexture("res/up.jpg"));
    textures.add(loadTexture("res/down.jpg"));
    textures.add(loadTexture("res/back.jpg"));
    textures.add(loadTexture("res/front.jpg"));
  }

  private Texture loadTexture(String filename) {
    return new Texture(new File(filename));
  }

  @Override
  public Texture getTexture(int i) {
    return textures.get(i);
  }

}
