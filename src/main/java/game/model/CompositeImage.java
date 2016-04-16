package game.model;

import game.image.Image;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

public class CompositeImage implements Image {
  
  static class Offset {
    int ox;
    int oy;
    
    public Offset(int ox, int oy) {
      this.ox = ox;
      this.oy = oy;
    }
  };
  
  Map<String, Offset> offsets = Maps.newHashMap(); 
  int width;
  int height;
  byte[] bytes;

  public CompositeImage(List<String> imageList, Function<String, Image> imageProvider) {
    width = 0;
    height = 0;

    for(String imageName: imageList) {
      Image image = imageProvider.apply(imageName);
      offsets.put(imageName, new Offset(width, 0));
      height = Math.max(height, image.getHeight());
      width += image.getWidth();
    }
    
    bytes = new byte[width * height * 4];
    for(int i=0;i<bytes.length;++i) {
      bytes[i] = 0;
    }
    
    for(String imageName: imageList) {
      Image image = imageProvider.apply(imageName);
      Offset offset = offsets.get(imageName);
      int oy = offset.oy;
      int ox = offset.ox;
      byte[] imgBytes = image.getRGBA();
      for(int y = 0; y < image.getHeight(); ++y) {
        for(int x = 0; x < image.getWidth(); ++x) {
          int dstIndex = 4*((oy+y)*width+(ox+x));
          int srcIndex = 4*(y*image.getWidth()+x);
          bytes[dstIndex+0] = imgBytes[srcIndex+0];
          bytes[dstIndex+1] = imgBytes[srcIndex+1];
          bytes[dstIndex+2] = imgBytes[srcIndex+2];
          bytes[dstIndex+3] = imgBytes[srcIndex+3];
        }
      }
    }
  }

  @Override
  public int getWidth() {
    return width;
  }

  @Override
  public int getHeight() {
    return height;
  }

  @Override
  public byte[] getRGBA() {
    return bytes;
  }

  public double[] transform(int nv, int[] i, List<String> imageList, double t[]) {
    double tv[] = new double[nv*2];
    for(int v=0; v<nv; ++v) {
      Offset offset = offsets.get(imageList.get(i[v]));
      tv[v*2+0] = t[v*2+0] + offset.ox;   
      tv[v*2+1] = t[v*2+1] + offset.oy;   
    }
    return tv;
  }

}
