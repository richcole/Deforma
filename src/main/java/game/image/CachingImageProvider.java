package game.image;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

public class CachingImageProvider implements Function<String, Image> {
  
  private Map<String, Image> imgMap = Maps.newHashMap();
  private Function<String, Image> provider;

  public CachingImageProvider(Function<String, Image> provider) {
    this.provider = provider;
  }
  
  @Override
  public Image apply(String imageName) {
    Image img = imgMap.get(imageName);
    if ( img == null ) {
      img = provider.apply(imageName);
      imgMap.put(imageName, img);
    }
    return img;
  }

}
