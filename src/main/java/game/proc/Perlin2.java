package game.proc;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

public class Perlin2 {
  
  List<Signal> signals;
  
  public static class Signal {
    Random random = new Random();
    double values[];
    int size;
    
    Signal(int size) {
      this.size = size;
      values = new double[size*size];
      for(int i=0;i<size*size; ++i) {
        values[i] = random.nextDouble() - 0.5;
      }
    }
    
    double get(double x, double y, double v) {
      double n = size - 1;
      int cx = (int)(x * n);
      int cy = (int)(y * n);
      double ax = (x - cx/n)*n;
      double ay = (y - cy/n)*n;
      double nax = 1 - ax;
      double nay = 1 - ay;
      double r1 = (nax*nay) * values[cy*size + cx];
      double r2 = (ax*nay)  * values[cy*size + cx+1];
      double r3 = (nax*ay)  * values[(cy+1)*size + cx];
      double r4 = (ax*ay)   * values[(cy+1)*size + cx+1];
      return v * (r1 + r2 + r3 + r4) / 4 / n;
    }
    
  }
  
  public Perlin2(int numSignals) {
    signals = Lists.newArrayList();
    for(int i=4;i<=numSignals; ++i) {
      signals.add(new Signal(i));
    }
  }
  
  double get(double x, double y, double v) {
    double r = 0;
    for(Signal signal: signals) {
      r += signal.get(x, y, v);
    }
    return r;
  }
  
  public void generate(HeightMap heightMap, double maxHeight) {
    double w = heightMap.getWidth();
    double d = heightMap.getDepth();
    for(int x=0;x<w;x++) {
      for(int y=0;y<d;y++) {
        heightMap.set(x, y, get(x / w, y / d, maxHeight));
      }
    }
  }

}
