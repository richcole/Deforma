package game;

import java.util.function.BiConsumer;

public class KdTree<T> {

  private static class Node<T> {
    int d;
    Vector v;
    T t;
    Node<T> left;
    Node<T> right;
    
    Node(int d, Vector v, T t) {
      this.d = d;
      this.v = v;
      this.t = t;
    }
  }
  
  public static class NN<T> {
    Node<T> n1;
    Node<T> n2;
    double ds1;
    double ds2;
    
    NN() {
    }
    
    public void add(Node<T> n, double ds) {
      if ( n1 == null || ds < ds1 ) {
        n2 = n1;
        ds2 = ds1;
        n1 = n;
        ds1 = ds;
      }
      else if ( n2 == null || ds < ds2 ) {
        n2 = n;
        ds2 = ds;
      }
    }
    
    public Vector getV1() {
      return n1 != null ? n1.v : null;
    }
    
    public T getT1() {
      return n1 != null ? n1.t : null;
    }

    public Vector getV2() {
      return n2 != null ? n2.v : null;
    }
    
    public T getT2() {
      return n2 != null ? n2.t : null;
    }
}
  
  Node<T> root;
  
  public void insert(Vector v, T t) {
    if ( root == null ) {
      root = new Node<T>(0, v, t);
    }
    else {
      insert(root, v, t);
    }
  }
  
  public void eachIn(BBox box, BiConsumer<Vector, T> visitor) {
    if ( root != null ) {
      eachIn(box, visitor, root);
    }
  }
  
  public NN<T> nn(Vector v) {
    if ( root != null ) {
      NN<T> nn = new NN<T>();
      nn(v, nn, root);
      return nn;
    }
    else {
      return null;
    }
  }
  
  public void nn(Vector v, NN<T> nn, Node<T> n) {
    double ns = n.v.distSquared(v);
    nn.add(n, ns);
    double dv = getValue(n, v);
    double nv = getValue(n, n.v);
    if ( dv < nv ) {
      if ( n.left != null ) {
        nn(v, nn, n.left);
      }
      if ( n.right != null && nn.n2 != null && Utils.squared(nv - dv) < nn.ds2 ) {
        nn(v, nn, n.right);
      }
    }
    else {
      if ( n.right != null ) {
        nn(v, nn, n.right);
      }
      if ( n.left != null && nn.n2 != null && Utils.squared(dv - nv) <= nn.ds2 ) {
        nn(v, nn, n.left);
      }
    }
  }
  
  public void eachIn(BBox box, BiConsumer<Vector, T> visitor, Node<T> n) {
    if ( box.contains(n.v) ) {
      visitor.accept(n.v, n.t);
    }
    if ( n.left != null && ! (getValue(n, box.lower) > getValue(n, n.v)) ) {
      eachIn(box, visitor, n.left);
    }
    if ( n.right != null && ! (getValue(n, box.upper) < getValue(n, n.v)) ) {
      eachIn(box, visitor, n.right);
    }
  }
  
  public double getValue(Node<T> n, Vector v) {
    return v.getValue(n.d);
  }

  
  void insert(Node<T> n, Vector v, T t) {
    if ( leq(n, v) ) {
      if ( n.left == null ) {
        n.left = new Node<T>((n.d + 1 % 3), v, t);
      }
      else {
        insert(n.left, v, t);
      }
    }
    else {
      if ( n.right == null ) {
        n.right = new Node<T>((n.d + 1 % 3), v, t);
      }
      else {
        insert(n.right, v, t);
      }
    }
  }
  
  private boolean leq(Node<T> n, Vector v) {
    return v.getValue(n.d) < n.v.getValue(n.d);
  }

}
