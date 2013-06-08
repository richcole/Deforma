package game.nwn;

import game.Context;
import game.base.Texture;

public class NwnTextureProvider implements TextureProvider {

  private Context context;
  private String name;

  public NwnTextureProvider(Context context, String name) {
    this.context = context;
    this.name = name;
  }

  public Texture createTexture() {
    return new Texture(context.getKeyReader().getImage(name));
  }

}
