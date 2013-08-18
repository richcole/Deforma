package game;

import game.voxel.Point;

import org.junit.Test;

public class TestPoint {

  
  @Test
  public void testPoint() {
    Point pt = new Point();
    for(int i=0;i<30;++i) {
      System.out.println(pt.getVector());
      pt.incr();
    }
  }
}
