package game.image;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.function.Function;

import javax.imageio.ImageIO;

import com.google.common.base.Preconditions;
import com.google.common.io.Resources;

public class ResourceImageProvider implements Function<String, Image> {

  @Override
  public Image apply(String path) {
      try {
        URL url = Resources.getResource(path);
        Preconditions.checkNotNull(url);
        BufferedImage img = ImageIO.read(url);
        return new BufferedImageImage(img);
      } catch(IOException e) {
        throw new RuntimeException("Couldn't load " + path, e);
      }
    }

}
