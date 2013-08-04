package game.voxel;

import java.util.Map;

import com.google.common.collect.Maps;

import game.math.Vector;
import game.proc.VertexCloud;

// the solver for the center point of the cubes in currently
// avg of center point plus edge crossing. It results in a pretty poor
// tesselation
public class DualContouring implements Tessellation {
  
  final static Vector[] UNITS = {
    new Vector(  1,   0,   0,  1), // 0
    new Vector(  0,   1,   0,  1), // 1
    new Vector(  0,   0,   1,  1), // 2
    new Vector( -1,   0,   0,  1), // 0
    new Vector(  0,  -1,   0,  1), // 1
    new Vector(  0,   0,  -1,  1), // 2
  };
  
  final static Vector[] DIRNS = {
    new Vector( 1,  0,  0,  1), // 0
    new Vector( 0,  1,  0,  1), // 1
    new Vector( 0,  0,  1,  1), // 2
    new Vector( -1,  0,  0,  1), // 0
    new Vector( 0,  -1,  0,  1), // 1
    new Vector( 0,  0,  -1,  1), // 2
  };

  final static Vector[] VERTICIES = {
    new Vector(-1, -1, -1, 1), // 0
    
    new Vector( 1, -1, -1, 1), // 1
    new Vector(-1,  1, -1, 1), // 2
    new Vector(-1, -1,  1, 1), // 3
    
    new Vector(-1,  1,  1, 1), // 4
    new Vector( 1, -1,  1, 1), // 5
    new Vector( 1,  1, -1, 1), // 6
    
    new Vector( 1,  1,  1, 1)  // 7
  };
  
  final static int[][] EDGES = {
    {0, 1},
    {0, 2},
    {0, 3},

    {4, 7},
    {4, 2},
    {4, 3},
    
    {5, 7},
    {5, 1},
    {5, 3},
    
    {6, 7},
    {6, 1},
    {6, 2},
  };

  static final int[][] EDGE_UNITS = new int[EDGES.length][];
  static final boolean[] DRAW_EDGE = new boolean[EDGES.length];
  
  static {
    
    for(int i=0;i<EDGES.length;++i) {
      EDGE_UNITS[i] = new int[2];
      int k = 0;
      for(int j=0;j<3;++j) {
        double c1 = VERTICIES[EDGES[i][0]].get(j);
        double c2 = VERTICIES[EDGES[i][1]].get(j);
        if ( c1 == c2 ) {
          if ( c1 > 0 ) {
            EDGE_UNITS[i][k++] = j;
          } else {
            EDGE_UNITS[i][k++] = 3+j;
          }
        }
      }
    }
    
    for(int i=0;i<EDGES.length;++i) {
      int u1 = EDGE_UNITS[i][0];
      int u2 = EDGE_UNITS[i][1];
      DRAW_EDGE[i] =  u1 < 3 && u2 < 3 || u1 >= 3 && u2 >= 3 ;
    }
    
    int x = 1;
  }
  
  public DualContouring() {
  }
  
  @Override
  public void genCloud(VertexCloud cloud, Vector bottomLeft, Vector topRight, DensityFunction densityFunction,
    Transform tr) {
    double grid[] = new double[8];
    Vector vs[] = new Vector[12];
    Vector ev[] = new Vector[12];
    Map<Vector, Vector> vm = Maps.newHashMap();
    
    for (double x = bottomLeft.x(); x < topRight.x(); ++x) {
      for (double y = bottomLeft.y(); y < topRight.y(); ++y) {
        for (double z = bottomLeft.z(); z < topRight.z(); ++z) {
          Vector p = new Vector(x, y, z, 1);
          for(int i=0; i<VERTICIES.length; ++i) {
            vs[i] = p.plus(VERTICIES[i].times(0.5));
            grid[i] = densityFunction.getDensity(vs[i]);
          }
          
          int evIndex = 0;
          Vector q = p;
          for(int i=0; i<EDGES.length; ++i) {
            double d1 = grid[EDGES[i][0]];
            double d2 = grid[EDGES[i][1]];
            if ( d1 * d2 < 0 ) {
              Vector mp = mix(d1, d2, vs[EDGES[i][0]], vs[EDGES[i][1]]);
              ev[evIndex++] = mp;
              q = q.plus(mp);
            }
          }
          if ( evIndex > 0 ) {
            q = q.times(1.0/(1.0+evIndex));
            if ( q.minus(p).length() > 1.0 ) {
              throw new RuntimeException("Q moved too far");
            }
            vm.put(p, q);
          }
          
        }
      }
    }
    
    for (double x = bottomLeft.x(); x < topRight.x(); ++x) {
      for (double y = bottomLeft.y(); y < topRight.y(); ++y) {
        for (double z = bottomLeft.z(); z < topRight.z(); ++z) {
          Vector p = new Vector(x, y, z, 1);
          for(int i=0; i<VERTICIES.length; ++i) {
            vs[i] = p.plus(VERTICIES[i].times(0.5));
            grid[i] = densityFunction.getDensity(vs[i]);
          }
          
          Vector p1 = new Vector(x, y, z, 1);
          Vector q1 = vm.get(p1);
          if ( q1 != null ) {
            for(int i=0; i<EDGES.length; ++i) {
              if ( DRAW_EDGE[i] ) {
                double d1 = grid[EDGES[i][0]];
                double d2 = grid[EDGES[i][1]];
                if ( d1 * d2 < 0 ) {
                  Vector u1 = UNITS[EDGE_UNITS[i][0]];
                  Vector u2 = UNITS[EDGE_UNITS[i][1]];
                  Vector q2 = vm.get(p1.plus(u1));
                  Vector q3 = vm.get(p1.plus(u2));
                  if ( q1 != null && q2 != null && q3 != null ) {
                    cloud.addVertex(tr.transform(q1), Vector.ZERO, Vector.ZERO);
                    cloud.addVertex(tr.transform(q2), Vector.ZERO, Vector.ZERO);
                    cloud.addVertex(tr.transform(q3), Vector.ZERO, Vector.ZERO);
                  }
                }
              }
            }
          }
        }      
      }
    }
    cloud.computeNormals();
  }

  private Vector mix(double d1, double d2, Vector p1, Vector p2) {
    return p1.plus(p2.minus(p1).times(d1 / (d1 - d2)));
  }

}
