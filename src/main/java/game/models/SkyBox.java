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
      loadTexture("front.jpg"),
      loadTexture("back.jpg"),
      loadTexture("left.jpg"),
      loadTexture("right.jpg"),
      loadTexture("up.jpg"),
      loadTexture("down.jpg")
     );
  }

  private Texture loadTexture(String filename) {
    return context.getTextures().getFileTexture(filename);
  }

}
