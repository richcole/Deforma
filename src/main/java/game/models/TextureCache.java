package game.models;

import game.gl.GLTexture;

import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

public class TextureCache {
  
  Map<String, GLTexture> textureMap = Maps.newHashMap();

  public GLTexture get(String textureName, Supplier<GLTexture> textureSupplier) {
    GLTexture texture = textureMap.get(textureName);
    if ( texture == null ) {
      texture = textureSupplier.get();
      textureMap.put(textureName, texture);
    }
    return texture;
  }

}
