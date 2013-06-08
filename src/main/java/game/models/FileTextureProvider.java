package game.models;

import game.base.Texture;
import game.nwn.TextureProvider;

import java.io.File;

public class FileTextureProvider implements TextureProvider {

  private String fileName;

  public FileTextureProvider(String fileName) {
    this.fileName = fileName;
  }
  
  @Override
  public Texture createTexture() {
    return new Texture(new File(fileName));
  }

}
