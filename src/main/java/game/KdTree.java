package game;

import java.util.function.BiConsumer;

public class KdTree<T> {

  private static class Node<T> {
    int d;
    Vector v;
    T t;
    Node left;
    Node right;
    
    Node(int d, Vector v, T t) {
      this.d = d;
      this.v = v;
    }
  }
  
  Node root;
  
  public void insert(Vector v) {
    if ( root == null ) {
      root = new Node(0, v);
    }
    else {
      insert(root, v);
    }
  }
  
  public void eachIn(BBox box, BiConsumer<Vector, T> visitor) {
    if ( root != null ) {
      eachIn(box, visitor, root);
    }
  }
  
  public void eachIn(BBox box, BiConsumer<Vector, T> visitor, Node n) {
    if ( box.contains(n.v) ) {
      visitor.accept(v, t);
    }
    
  }

  
  void insert(Node n, Vector v) {
    if ( leq(n, v) ) {
      if ( n.left == null ) {
        n.left = new Node((n.d + 1 % 3), v);
      }
      else {
        insert(n.left, v);
      }
    }
    else {
      if ( n.right == null ) {
        n.right = new Node((n.d + 1 % 3), v);
      }
      else {
        insert(n.right, v);
      }
    }
  }
  
  private boolean leq(Node n, Vector v) {
    return (n.v.v[n.d] / n.v.v[3]) < v.v[n.d] / v.v[3];
  }

}
