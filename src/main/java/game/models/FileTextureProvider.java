package game.models;

import game.base.Image;
import game.base.Texture;
import game.imageio.TgaLoader;
import game.nwn.TextureProvider;
import game.nwn.readers.BinaryFileReader;

import java.io.File;

public class FileTextureProvider implements TextureProvider {

  private String fileName;

  public FileTextureProvider(String fileName) {
    this.fileName = fileName;
  }
  
  @Override
  public Texture createTexture() {
    if ( fileName.endsWith(".tga") ) {
      TgaLoader imageLoader = new TgaLoader();
      BinaryFileReader reader = new BinaryFileReader(new File(fileName));
      try {
        Image image = imageLoader.readImage(reader, 0);
        return new Texture(image);
      } finally {
        reader.close();
      }
    } else {
      return new Texture(new File(fileName));
    }
  }

}
