package game.models;

import game.base.Image;
import game.base.Texture;
import game.imageio.TgaLoader;
import game.nwn.TextureProvider;
import game.nwn.readers.BinaryFileReader;

import java.io.File;

public class FileTextureProvider implements TextureProvider {

  private File file;

  public FileTextureProvider(File file) {
    this.file = file;
  }
  
  @Override
  public Texture createTexture() {
    if ( file.getName().endsWith(".tga") ) {
      TgaLoader imageLoader = new TgaLoader();
      BinaryFileReader reader = new BinaryFileReader(file);
      try {
        Image image = imageLoader.readImage(reader, 0);
        return new Texture(image);
      } finally {
        reader.close();
      }
    } else {
      return new Texture(file);
    }
  }

}
