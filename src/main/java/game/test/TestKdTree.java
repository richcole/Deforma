package game.test;

import game.BiConsumerCounter;
import game.KdTree;
import game.basicgeom.BBox;
import game.basicgeom.Vector;

import org.junit.Assert;
import org.junit.Test;

public class TestKdTree {

  @Test
  public void testEmptyTree() {
    KdTree<Integer> tree = new KdTree<Integer>();
    BBox box = new BBox(Vector.Z, Vector.Z);
    tree.eachIn(box, (v, t) -> { throw new RuntimeException("No match expected"); });
  }

  @Test
  public void testZero() {
    KdTree<Integer> tree = new KdTree<Integer>();
    BBox box = new BBox(Vector.Z, Vector.Z);
    tree.insert(Vector.Z, 1);
    BiConsumerCounter<Vector, Integer> counter = new BiConsumerCounter<Vector, Integer>();
    tree.eachIn(box, counter);
    Assert.assertEquals(1, counter.getCount());
  }

  @Test
  public void testOne() {
    KdTree<Integer> tree = new KdTree<Integer>();
    BBox box = new BBox(Vector.Z, Vector.U1);
    tree.insert(Vector.Z, 1);
    tree.insert(Vector.U1, 1);
    BiConsumerCounter<Vector, Integer> counter = new BiConsumerCounter<Vector, Integer>();
    tree.eachIn(box, counter);
    Assert.assertEquals(2, counter.getCount());
  }

  @Test
  public void testOneZ() {
    KdTree<Integer> tree = new KdTree<Integer>();
    BBox box = new BBox(Vector.Z, Vector.Z);
    tree.insert(Vector.Z, 1);
    tree.insert(Vector.U1, 1);
    BiConsumerCounter<Vector, Integer> counter = new BiConsumerCounter<Vector, Integer>();
    tree.eachIn(box, counter);
    Assert.assertEquals(1, counter.getCount());
  }

  @Test
  public void testNN1() {
    KdTree<Integer> tree = new KdTree<Integer>();
    tree.insert(Vector.Z, 1);
    KdTree.NN<Integer> nn = tree.nn(Vector.Z);
    Assert.assertEquals(Integer.valueOf(1), nn.getT1());
  }

  @Test
  public void testNN2() {
    KdTree<Integer> tree = new KdTree<Integer>();
    tree.insert(Vector.Z, 1);
    tree.insert(Vector.U1, 2);
    KdTree.NN<Integer> nn = tree.nn(Vector.Z);
    Assert.assertEquals(Integer.valueOf(1), nn.getT1());
    Assert.assertEquals(Integer.valueOf(2), nn.getT2());
  }

  @Test
  public void testNN3() {
    KdTree<Integer> tree = new KdTree<Integer>();
    tree.insert(Vector.Z, 1);
    tree.insert(Vector.U1, 2);
    KdTree.NN<Integer> nn = tree.nn(Vector.U1);
    Assert.assertEquals(Integer.valueOf(2), nn.getT1());
    Assert.assertEquals(Integer.valueOf(1), nn.getT2());
  }
}
