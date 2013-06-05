package game.base;

import game.nwn.TextureProvider;

import java.util.Map;

import com.google.common.collect.Maps;

public class Textures {

  Map<String, Texture> textures = Maps.newHashMap();
  
  public Texture getTexture(String name, TextureProvider textureProvider) {
    Texture texture = textures.get(name);
    if ( texture == null ) {
      texture = textureProvider.createTexture();
      textures.put(name, texture);
    }
    return texture;
  }
  
}
