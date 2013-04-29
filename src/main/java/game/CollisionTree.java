package game;

import game.CollisionTree.Node;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class CollisionTree {
  
  Node root = new Node();

  static class Node {
    Node[] children;
    List<Object> objects = Lists.newArrayList();
    
    // add max depth to the tree
    // convert oMin and oMax be Vector3f
    // lowerIndex, upperIndex become 3 vectors
    
    public void insert(int radix, float oMin, float oMax, Object o) {
      if ( oMin <= 0 && oMax >= 1 ) {
        objects.add(o);
      }
      else {
        oMin = Math.max(oMin, 0);
        oMax = Math.min(oMax, 1);
        int lowerIndex = (int) Math.floor(oMin * radix);
        int upperIndex = (int) Math.floor(oMax * radix);
        for(int i=lowerIndex; i<=upperIndex && i<radix; ++i) {
          ensureChild(radix, i).insert(radix, oMin*radix - i, oMax*radix - (i+1), o);
        }
      }
    }

    private Node ensureChild(int radix, int i) {
      if ( children == null ) {
        children = new Node[radix];
      }
      if ( children[i] == null ) {
        children[i] = new Node();
      }
      return children[i];
    }

    private Node getChild(int radix, int i) {
      if ( children == null ) {
        return null;
      }
      return children[i];
    }

    public void lookup(int radix, float oMin, float oMax, Set<Object> result) {
      result.addAll(objects);
      oMin = Math.max(oMin, 0);
      oMax = Math.min(oMax, 1);
      int lowerIndex = (int) Math.floor(oMin * radix);
      int upperIndex = (int) Math.floor(oMax * radix);
      for(int i=lowerIndex; i<=upperIndex; ++i) {
        Node child = getChild(radix, i);
        if ( child != null ) {
          child.lookup(radix, oMin*radix - i, oMax*radix - (i+1), result);
        }
      }
    }
  }
  
  CollisionTree() {
  }
  
  public void insert(float oMin, float oMax, Object o) {
    root.insert(10, oMin, oMax, o);
  }
  
  public Set<Object> lookup(float oMin, float oMax) {
    Set<Object> objects = Sets.newHashSet();
    root.lookup(10, oMin, oMax, objects);
    return objects;
  }
}
