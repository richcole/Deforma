package game;

import game.math.Vector;
import game.voxel.OctTree;
import game.voxel.PotentialField;

import org.junit.Test;

public class TestOctTree {

  
  @Test
  public void testOctTree() {
    double radius = 50;
    Vector center = Vector.ONES.times(radius);
    OctTree octTree = new OctTree(center, radius, 6, 2);
    PotentialField s1 = new PotentialField(center, radius/2);
    octTree.add(s1);
  }
}
