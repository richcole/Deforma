package game.voxel;

import java.util.Set;

import com.google.common.collect.Sets;

import game.math.Vector;
import game.proc.VertexCloud;

public class OctTree {
  
  final static Vector[] OFFSETS = ShapeRenderer.CUBE_VERTEXES;
  
  public static class Node {

    Node parent;
    Node[] children;
    double density;
    boolean isBoundary;
    Vector surfacePoint;
    Vector center;
    double depth;
    Set<Node> adjs;

    public Node(Node parent, double depth) {
      this.parent = parent;
      this.depth = depth;
    }
    
    public void addAdj(Node node) {
      if ( adjs == null ) {
        this.adjs = Sets.newHashSet();
      }
      adjs.add(node);
    }

    public boolean isAdj(Node n) {
      return adjs != null && adjs.contains(n);
    }
      
  }
  
  Node root;
  Vector center;
  double radius;
  double maxDepth;
  double minDepth;
  double epsilon;
  
  ShapeRenderer renderer;
  
  public OctTree(Vector center, double radius, double maxDepth, double minDepth) {
    this.center = center;
    this.radius = radius;
    this.maxDepth = maxDepth;
    this.minDepth = minDepth;
    this.epsilon = radius / Math.pow(2, maxDepth+1);
    this.renderer = new ShapeRenderer();
  }

  public void add(DensityFunction densityFunction) {
    root = generate(null, root, densityFunction, center, radius, 0);
  }
  
  private Node generate(Node parent, Node node, DensityFunction densityFunction, Vector center, double radius, double depth) {
    double d = densityFunction.getDensity(center); 
    if ( node == null ) {
      node = new Node(parent, depth);
      node.density = d;
      node.isBoundary = renderer.isBoundaryCube(center, radius, densityFunction);
      node.surfacePoint = renderer.centerPoint(center, radius, densityFunction);
    }
    if ( depth < maxDepth ) {
      double densities[] = new double[OFFSETS.length];
      Vector centers[] = new Vector[OFFSETS.length];
      int numPos = 0;
      for(int i=0; i<OFFSETS.length; ++i) {
        centers[i] = getChildCenter(center, radius, i);
        densities[i] = densityFunction.getDensity(centers[i]);
        if ( Funs.isPos(densities[i]) ) {
          ++numPos;
        }
      }
      if ( (numPos != OFFSETS.length && numPos != 0) || depth < minDepth ) {
        if ( node.children == null ) {
          node.children = new Node[OFFSETS.length];
        }
        for(int i=0; i<OFFSETS.length; ++i) {
          node.children[i] = generate(node, node.children[i], densityFunction, centers[i], radius/2, depth+1);
          node.children[i].center = centers[i];
        }
      }
    }
    return node;
  }
  
  void renderToVertexCloud(VertexCloud cloud, Transform transform, DensityFunction f) {
    findAdj(root, center, radius, f);
    renderToVertexCloud(cloud, root, center, radius, transform);
  }
  
  public void findAdj(Node node, Vector center, double radius, DensityFunction f) {
    if ( node.children == null ) {
      if ( node.isBoundary ) {
        double[] ds = new double[ShapeRenderer.CUBE_VERTEXES.length];
        for(int i=0;i<ShapeRenderer.CUBE_VERTEXES.length;++i) {
          ds[i] = f.getDensity(renderer.getVertex(center, radius, i));
        }
        for(int i=0;i<ShapeRenderer.CUBE_ADJ_CENTERS.length; ++i) {
          int[][] edges = ShapeRenderer.CUBE_ADJ_EDGES[i];
          Node adjNode = getNode(center.plus(ShapeRenderer.CUBE_ADJ_CENTERS[i].times(radius+epsilon)));
          if ( adjNode != null && adjNode.isBoundary && node.depth >= adjNode.depth ) {
            if ( renderer.hasEdgeCrossing(edges, ds) ) {
              node.addAdj(adjNode);
              adjNode.addAdj(node);
            }
          }
        }
      }
    } else {
      for(Node child: node.children) {
        findAdj(child, child.center, radius/2, f);
      }
    }
  }

  public void renderToVertexCloud(VertexCloud cloud, Node node, Vector center, double radius, Transform tr) {
    if ( node.children == null ) {
      Node[]   adjNodes = new Node[100];
      int      adjIndex = 0;
      double[] adjAngle = new double[adjNodes.length];
      if ( node.adjs != null ) {
        for(Node n1: node.adjs) {
          if ( n1.hashCode() > node.hashCode() ) {
            continue;
          }
          adjNodes[adjIndex] = n1;
          adjAngle[adjIndex] = adjNodes[0].surfacePoint.minus(node.surfacePoint).dot(adjNodes[adjIndex].surfacePoint.minus(node.surfacePoint));
          adjIndex += 1;
        }
        for(int i=1;i<adjIndex;++i) {
          for(int j=i+1;j<adjIndex;++j) {
            if ( adjAngle[j] < adjAngle[i] ) {
              swap(adjNodes, adjAngle, i, j);
            }
          }
        }
        for(int i=1;i<adjIndex;++i) {
          cloud.addTriangle(node.surfacePoint, adjNodes[i-1].surfacePoint, adjNodes[i].surfacePoint, tr);
        }
      }
    }
    else {
      for(int i=0; i<OFFSETS.length; ++i) {
        Node child = node.children[i];
        Vector childCenter = getChildCenter(center, radius, i);
        renderToVertexCloud(cloud, child, childCenter, radius/2, tr);
      }
    }
  }

  private void swap(Node[] adjNodes, double[] adjAngle, int i, int j) {
    Node tmpNode = adjNodes[i];
    double tmpAngle = adjAngle[i];
    adjNodes[i] = adjNodes[j];
    adjAngle[i] = adjAngle[j];
    adjNodes[j] = tmpNode;
    adjAngle[j] = tmpAngle;
  }

  private Node getNode(Vector p) {
    return getNode(root, center, radius, p);
  }

  private Node getNode(Node node, Vector center, double radius, Vector p) {
    if ( node.children != null ) {
      for(Node child: node.children) {
        if ( Funs.inSquare(child.center, radius/2, p) ) {
          return getNode(child, child.center, radius/2, p);
        }
      }
      return null;
    } else {
      return node;
    }
  }

  private Vector getChildCenter(Vector center, double radius, int i) {
    return center.plus(OFFSETS[i].times(radius/2));
  }

}
