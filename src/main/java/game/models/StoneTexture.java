package game.models;

import game.Context;
import game.Texture;

import java.io.File;

public class StoneTexture extends Texture {

  public StoneTexture(Context context) {
    super(new File("res/image.jpg"));
  }

}
