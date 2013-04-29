package game;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

public class CollisionTreeTest {

  @Test
  public void testCollisionTree1() {
    CollisionTree tree = new CollisionTree();
    tree.insert(0.1f, 0.6f, "one");
    Set<Object> o = tree.lookup(0.2f, 0.2f);
    Assert.assertEquals("one", o.iterator().next());
  }

  @Test
  public void testCollisionTree2() {
    CollisionTree tree = new CollisionTree();
    tree.insert(0.1576f, 0.6f, "one");
    Set<Object> o = tree.lookup(0.2f, 0.2f);
    Assert.assertEquals("one", o.iterator().next());
  }

  @Test
  public void testCollisionTree3() {
    CollisionTree tree = new CollisionTree();
    tree.insert(0.1576f, 0.6f, "one");
    Set<Object> o = tree.lookup(0.65f, 0.7f);
    Assert.assertEquals(0, o.size());
  }
}
