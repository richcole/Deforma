package game.voxel;

import java.util.Random;

import game.math.Vector;
import game.proc.VertexCloud;

public class ShapeRenderer {
  public static Vector[] CUBE_VERTEXES = {
    Vector.U1.plus(Vector.U2).plus(Vector.U3), // 0
    
    Vector.M1.plus(Vector.U2).plus(Vector.U3), // 1
    Vector.U1.plus(Vector.M2).plus(Vector.U3), // 2
    Vector.U1.plus(Vector.U2).plus(Vector.M3), // 3
    
    Vector.U1.plus(Vector.M2).plus(Vector.M3), // 4
    Vector.M1.plus(Vector.U2).plus(Vector.M3), // 5
    Vector.M1.plus(Vector.M2).plus(Vector.U3), // 6
    
    Vector.M1.plus(Vector.M2).plus(Vector.M3)  // 7
  };
  
  public static int[][][] CUBE_ADJ_EDGES = {
    { {0, 1} },
    { {2, 4} },
    { {3, 5} },
    { {4, 7} },

    {},
    {},
    {},
    {},

    {},
    {},
    {},
    {},

    {},
    {},
    {},
    {},
    {},
    {}
  };
  
  public static Vector[] CUBE_ADJ_CENTERS = {
    Vector.Z.plus(Vector.U2).plus(Vector.U3), 
    Vector.Z.plus(Vector.M2).plus(Vector.U3), 
    Vector.Z.plus(Vector.U2).plus(Vector.M3), 
    Vector.Z.plus(Vector.M2).plus(Vector.M3), 

    Vector.U1.plus(Vector.Z).plus(Vector.U3), 
    Vector.M1.plus(Vector.Z).plus(Vector.U3), 
    Vector.U1.plus(Vector.Z).plus(Vector.M3), 
    Vector.M1.plus(Vector.Z).plus(Vector.M3), 

    Vector.U1.plus(Vector.U2).plus(Vector.Z), 
    Vector.U1.plus(Vector.M2).plus(Vector.Z), 
    Vector.M1.plus(Vector.U2).plus(Vector.Z), 
    Vector.M1.plus(Vector.M2).plus(Vector.Z), // 12 edge center pieces
    
    Vector.Z.plus(Vector.Z).plus(Vector.U3),   
    Vector.Z.plus(Vector.Z).plus(Vector.M3), 

    Vector.Z.plus(Vector.U2).plus(Vector.Z), 
    Vector.Z.plus(Vector.M2).plus(Vector.Z), 

    Vector.U1.plus(Vector.Z).plus(Vector.Z), 
    Vector.M1.plus(Vector.Z).plus(Vector.Z)  // 6 face centers 
  };

  public static int[][] CUBE_FACE_EDGES = {
    { 0, 2, 3, 4 },
    { 1, 5, 6, 7 },
    
    { 0, 1, 3, 5 },
    { 2, 4, 6, 7 },
    
    { 0, 1, 2, 6 },
    { 3, 4, 5, 7 } 
  };
  
  public static Vector[] CUBE_FACE_CENTERS = {
    Vector.U1,
    Vector.U2,
    Vector.U3,
    Vector.M1,
    Vector.M2,
    Vector.M3,
  };

  public static Vector[][] CUBE_FACES_VECTORS = {
    { Vector.U2, Vector.U3 },
    { Vector.U1, Vector.U3 },
    { Vector.U1, Vector.U2 },
    { Vector.M2, Vector.M3 },
    { Vector.M1, Vector.M3 },
    { Vector.M1, Vector.M2 },
  };
  
  public boolean isBoundaryCube(Vector center, double radius, DensityFunction f) {
    int numPos = 0;
    for(Vector v: CUBE_VERTEXES) {
      Vector p = center.plus(v.times(radius));
      if ( Funs.isPos(f.getDensity(p)) ) {
        numPos += 1;
      }
    }
    return numPos != 0 && numPos != CUBE_VERTEXES.length;
  }

  public void addCube(VertexCloud cloud, Vector center, double radius, Transform tr) {
    for(int i=0; i<6; ++i) {
      Vector dp = center.plus(CUBE_FACE_CENTERS[i].times(radius));
      Vector v1 = CUBE_FACES_VECTORS[i][0].times(radius);
      Vector v2 = CUBE_FACES_VECTORS[i][1].times(radius);

      Random r = new Random();
      Vector n = new Vector(r.nextDouble(), r.nextDouble(), r.nextDouble(), 1).normalize();
      
      Vector p1 = dp.minus(v1).minus(v2);
      Vector p2 = dp.minus(v1).plus(v2);
      Vector p3 = dp.plus(v1).plus(v2);
      Vector p4 = dp.plus(v1).minus(v2);

      Vector n1 = p1.plus(dp).normalize();
      Vector n2 = p2.plus(dp).normalize();
      Vector n3 = p3.plus(dp).normalize();
      Vector n4 = p4.plus(dp).normalize();

      cloud.addVertex(tr.transform(p1), n, Vector.Z);
      cloud.addVertex(tr.transform(p2), n, Vector.Z);
      cloud.addVertex(tr.transform(p3), n, Vector.Z);

      cloud.addVertex(tr.transform(p1), n, Vector.Z);
      cloud.addVertex(tr.transform(p4), n, Vector.Z);
      cloud.addVertex(tr.transform(p3), n, Vector.Z);
    }
  }

  public Vector centerPoint(Vector center, double radius, DensityFunction f) {
    double ds[] = new double[CUBE_VERTEXES.length];
    Vector ps[] = new Vector[CUBE_VERTEXES.length];
    
    for(int i=0; i<CUBE_VERTEXES.length; ++i) {
      ps[i] = center.plus(CUBE_VERTEXES[i].times(radius));
      ds[i] = f.getDensity(ps[i]);
    }
    
    Vector s = Vector.Z;
    double ns = 0;
    for(int i=0; i<CUBE_FACE_EDGES.length; ++i) {
      int[] edges = CUBE_FACE_EDGES[i];
      int n = edges.length;
      for(int j=0; j<n; ++j) {
        int nj = (j+1) % n;
        double d1 = ds[j];
        double d2 = ds[nj];
        Vector p1 = ps[j];
        Vector p2 = ps[nj];
        if ( Funs.isPos(d1) != Funs.isPos(d2) ) {
          s = s.plus(Funs.midPoint(d1, d2, p1, p2));
          ns += 1;
        }
      }
    }    
    return s.times(1/ns);
  }

  public boolean hasEdgeCrossing(int[][] edges, double[] ds) {
    for(int i=0;i<edges.length;++i) {
      if ( hasEdgeCrossing(edges[i], ds) ) {
        return true;
      }
    }
    return false;
  }
  
  private boolean hasEdgeCrossing(int[] edges, double[] ds) {
    return Funs.isPos(ds[edges[0]]) != Funs.isPos(ds[edges[1]]);
  }

  public Vector getVertex(Vector center, double radius, int i) {
    return center.plus(ShapeRenderer.CUBE_VERTEXES[i].times(radius));
  }
  

}
