package game.models;

import game.Context;
import game.base.Texture;
import game.math.Vector;

import java.util.List;

import com.google.common.collect.Lists;

public class SkyBox extends Cube {
  
  final static double SIZE = 10000;
  
  List<Texture> textures;

  public SkyBox(Context context) {
    super(context, Vector.ZERO, SIZE);
  }

  @Override
  protected List<Texture> getTextures() {
    return Lists.newArrayList(
      loadTexture("res/front.jpg"),
      loadTexture("res/back.jpg"),
      loadTexture("res/left.jpg"),
      loadTexture("res/right.jpg"),
      loadTexture("res/up.jpg"),
      loadTexture("res/down.jpg")
     );
  }

  private Texture loadTexture(String filename) {
    return context.getTextures().getFileTexture(filename);
  }

}
