package game.voxel;

import game.Context;
import game.Registerable;
import game.Renderable;
import game.containers.Relation;
import game.math.Vector;
import game.proc.VertexCloud;
import game.shaders.ProgramRenderer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class CubeMap implements Renderable, Registerable {
  
  Vector dimScale;
  int mx, my, mz;
  double scale;
  VertexCloud cloud;
  Context context;
  ProgramRenderer program;
  List<DensityMap> densityMaps;
  
  static final long EDGE_TABLE[] = {
      0x0  , 0x109, 0x203, 0x30a, 0x406, 0x50f, 0x605, 0x70c,
      0x80c, 0x905, 0xa0f, 0xb06, 0xc0a, 0xd03, 0xe09, 0xf00,
      0x190, 0x99 , 0x393, 0x29a, 0x596, 0x49f, 0x795, 0x69c,
      0x99c, 0x895, 0xb9f, 0xa96, 0xd9a, 0xc93, 0xf99, 0xe90,
      0x230, 0x339, 0x33 , 0x13a, 0x636, 0x73f, 0x435, 0x53c,
      0xa3c, 0xb35, 0x83f, 0x936, 0xe3a, 0xf33, 0xc39, 0xd30,
      0x3a0, 0x2a9, 0x1a3, 0xaa , 0x7a6, 0x6af, 0x5a5, 0x4ac,
      0xbac, 0xaa5, 0x9af, 0x8a6, 0xfaa, 0xea3, 0xda9, 0xca0,
      0x460, 0x569, 0x663, 0x76a, 0x66 , 0x16f, 0x265, 0x36c,
      0xc6c, 0xd65, 0xe6f, 0xf66, 0x86a, 0x963, 0xa69, 0xb60,
      0x5f0, 0x4f9, 0x7f3, 0x6fa, 0x1f6, 0xff , 0x3f5, 0x2fc,
      0xdfc, 0xcf5, 0xfff, 0xef6, 0x9fa, 0x8f3, 0xbf9, 0xaf0,
      0x650, 0x759, 0x453, 0x55a, 0x256, 0x35f, 0x55 , 0x15c,
      0xe5c, 0xf55, 0xc5f, 0xd56, 0xa5a, 0xb53, 0x859, 0x950,
      0x7c0, 0x6c9, 0x5c3, 0x4ca, 0x3c6, 0x2cf, 0x1c5, 0xcc ,
      0xfcc, 0xec5, 0xdcf, 0xcc6, 0xbca, 0xac3, 0x9c9, 0x8c0,
      0x8c0, 0x9c9, 0xac3, 0xbca, 0xcc6, 0xdcf, 0xec5, 0xfcc,
      0xcc , 0x1c5, 0x2cf, 0x3c6, 0x4ca, 0x5c3, 0x6c9, 0x7c0,
      0x950, 0x859, 0xb53, 0xa5a, 0xd56, 0xc5f, 0xf55, 0xe5c,
      0x15c, 0x55 , 0x35f, 0x256, 0x55a, 0x453, 0x759, 0x650,
      0xaf0, 0xbf9, 0x8f3, 0x9fa, 0xef6, 0xfff, 0xcf5, 0xdfc,
      0x2fc, 0x3f5, 0xff , 0x1f6, 0x6fa, 0x7f3, 0x4f9, 0x5f0,
      0xb60, 0xa69, 0x963, 0x86a, 0xf66, 0xe6f, 0xd65, 0xc6c,
      0x36c, 0x265, 0x16f, 0x66 , 0x76a, 0x663, 0x569, 0x460,
      0xca0, 0xda9, 0xea3, 0xfaa, 0x8a6, 0x9af, 0xaa5, 0xbac,
      0x4ac, 0x5a5, 0x6af, 0x7a6, 0xaa , 0x1a3, 0x2a9, 0x3a0,
      0xd30, 0xc39, 0xf33, 0xe3a, 0x936, 0x83f, 0xb35, 0xa3c,
      0x53c, 0x435, 0x73f, 0x636, 0x13a, 0x33 , 0x339, 0x230,
      0xe90, 0xf99, 0xc93, 0xd9a, 0xa96, 0xb9f, 0x895, 0x99c,
      0x69c, 0x795, 0x49f, 0x596, 0x29a, 0x393, 0x99 , 0x190,
      0xf00, 0xe09, 0xd03, 0xc0a, 0xb06, 0xa0f, 0x905, 0x80c,
      0x70c, 0x605, 0x50f, 0x406, 0x30a, 0x203, 0x109, 0x0
  };
  
  static final byte TRI_TABLE[][] = {
    {},
    {0, 8, 3},
    {0, 1, 9},
    {1, 8, 3, 9, 8, 1},
    {1, 2, 10},
    {0, 8, 3, 1, 2, 10},
    {9, 2, 10, 0, 2, 9},
    {2, 8, 3, 2, 10, 8, 10, 9, 8},
    {3, 11, 2},
    {0, 11, 2, 8, 11, 0},
    {1, 9, 0, 2, 3, 11},
    {1, 11, 2, 1, 9, 11, 9, 8, 11},
    {3, 10, 1, 11, 10, 3},
    {0, 10, 1, 0, 8, 10, 8, 11, 10},
    {3, 9, 0, 3, 11, 9, 11, 10, 9},
    {9, 8, 10, 10, 8, 11},
    {4, 7, 8},
    {4, 3, 0, 7, 3, 4},
    {0, 1, 9, 8, 4, 7},
    {4, 1, 9, 4, 7, 1, 7, 3, 1},
    {1, 2, 10, 8, 4, 7},
    {3, 4, 7, 3, 0, 4, 1, 2, 10},
    {9, 2, 10, 9, 0, 2, 8, 4, 7},
    {2, 10, 9, 2, 9, 7, 2, 7, 3, 7, 9, 4},
    {8, 4, 7, 3, 11, 2},
    {11, 4, 7, 11, 2, 4, 2, 0, 4},
    {9, 0, 1, 8, 4, 7, 2, 3, 11},
    {4, 7, 11, 9, 4, 11, 9, 11, 2, 9, 2, 1},
    {3, 10, 1, 3, 11, 10, 7, 8, 4},
    {1, 11, 10, 1, 4, 11, 1, 0, 4, 7, 11, 4},
    {4, 7, 8, 9, 0, 11, 9, 11, 10, 11, 0, 3},
    {4, 7, 11, 4, 11, 9, 9, 11, 10},
    {9, 5, 4},
    {9, 5, 4, 0, 8, 3},
    {0, 5, 4, 1, 5, 0},
    {8, 5, 4, 8, 3, 5, 3, 1, 5},
    {1, 2, 10, 9, 5, 4},
    {3, 0, 8, 1, 2, 10, 4, 9, 5},
    {5, 2, 10, 5, 4, 2, 4, 0, 2},
    {2, 10, 5, 3, 2, 5, 3, 5, 4, 3, 4, 8},
    {9, 5, 4, 2, 3, 11},
    {0, 11, 2, 0, 8, 11, 4, 9, 5},
    {0, 5, 4, 0, 1, 5, 2, 3, 11},
    {2, 1, 5, 2, 5, 8, 2, 8, 11, 4, 8, 5},
    {10, 3, 11, 10, 1, 3, 9, 5, 4},
    {4, 9, 5, 0, 8, 1, 8, 10, 1, 8, 11, 10},
    {5, 4, 0, 5, 0, 11, 5, 11, 10, 11, 0, 3},
    {5, 4, 8, 5, 8, 10, 10, 8, 11},
    {9, 7, 8, 5, 7, 9},
    {9, 3, 0, 9, 5, 3, 5, 7, 3},
    {0, 7, 8, 0, 1, 7, 1, 5, 7},
    {1, 5, 3, 3, 5, 7},
    {9, 7, 8, 9, 5, 7, 10, 1, 2},
    {10, 1, 2, 9, 5, 0, 5, 3, 0, 5, 7, 3},
    {8, 0, 2, 8, 2, 5, 8, 5, 7, 10, 5, 2},
    {2, 10, 5, 2, 5, 3, 3, 5, 7},
    {7, 9, 5, 7, 8, 9, 3, 11, 2},
    {9, 5, 7, 9, 7, 2, 9, 2, 0, 2, 7, 11},
    {2, 3, 11, 0, 1, 8, 1, 7, 8, 1, 5, 7},
    {11, 2, 1, 11, 1, 7, 7, 1, 5},
    {9, 5, 8, 8, 5, 7, 10, 1, 3, 10, 3, 11},
    {5, 7, 0, 5, 0, 9, 7, 11, 0, 1, 0, 10, 11, 10, 0},
    {11, 10, 0, 11, 0, 3, 10, 5, 0, 8, 0, 7, 5, 7, 0},
    {11, 10, 5, 7, 11, 5},
    {10, 6, 5},
    {0, 8, 3, 5, 10, 6},
    {9, 0, 1, 5, 10, 6},
    {1, 8, 3, 1, 9, 8, 5, 10, 6},
    {1, 6, 5, 2, 6, 1},
    {1, 6, 5, 1, 2, 6, 3, 0, 8},
    {9, 6, 5, 9, 0, 6, 0, 2, 6},
    {5, 9, 8, 5, 8, 2, 5, 2, 6, 3, 2, 8},
    {2, 3, 11, 10, 6, 5},
    {11, 0, 8, 11, 2, 0, 10, 6, 5},
    {0, 1, 9, 2, 3, 11, 5, 10, 6},
    {5, 10, 6, 1, 9, 2, 9, 11, 2, 9, 8, 11},
    {6, 3, 11, 6, 5, 3, 5, 1, 3},
    {0, 8, 11, 0, 11, 5, 0, 5, 1, 5, 11, 6},
    {3, 11, 6, 0, 3, 6, 0, 6, 5, 0, 5, 9},
    {6, 5, 9, 6, 9, 11, 11, 9, 8},
    {5, 10, 6, 4, 7, 8},
    {4, 3, 0, 4, 7, 3, 6, 5, 10},
    {1, 9, 0, 5, 10, 6, 8, 4, 7},
    {10, 6, 5, 1, 9, 7, 1, 7, 3, 7, 9, 4},
    {6, 1, 2, 6, 5, 1, 4, 7, 8},
    {1, 2, 5, 5, 2, 6, 3, 0, 4, 3, 4, 7},
    {8, 4, 7, 9, 0, 5, 0, 6, 5, 0, 2, 6},
    {7, 3, 9, 7, 9, 4, 3, 2, 9, 5, 9, 6, 2, 6, 9},
    {3, 11, 2, 7, 8, 4, 10, 6, 5},
    {5, 10, 6, 4, 7, 2, 4, 2, 0, 2, 7, 11},
    {0, 1, 9, 4, 7, 8, 2, 3, 11, 5, 10, 6},
    {9, 2, 1, 9, 11, 2, 9, 4, 11, 7, 11, 4, 5, 10, 6},
    {8, 4, 7, 3, 11, 5, 3, 5, 1, 5, 11, 6},
    {5, 1, 11, 5, 11, 6, 1, 0, 11, 7, 11, 4, 0, 4, 11},
    {0, 5, 9, 0, 6, 5, 0, 3, 6, 11, 6, 3, 8, 4, 7},
    {6, 5, 9, 6, 9, 11, 4, 7, 9, 7, 11, 9},
    {10, 4, 9, 6, 4, 10},
    {4, 10, 6, 4, 9, 10, 0, 8, 3},
    {10, 0, 1, 10, 6, 0, 6, 4, 0},
    {8, 3, 1, 8, 1, 6, 8, 6, 4, 6, 1, 10},
    {1, 4, 9, 1, 2, 4, 2, 6, 4},
    {3, 0, 8, 1, 2, 9, 2, 4, 9, 2, 6, 4},
    {0, 2, 4, 4, 2, 6},
    {8, 3, 2, 8, 2, 4, 4, 2, 6},
    {10, 4, 9, 10, 6, 4, 11, 2, 3},
    {0, 8, 2, 2, 8, 11, 4, 9, 10, 4, 10, 6},
    {3, 11, 2, 0, 1, 6, 0, 6, 4, 6, 1, 10},
    {6, 4, 1, 6, 1, 10, 4, 8, 1, 2, 1, 11, 8, 11, 1},
    {9, 6, 4, 9, 3, 6, 9, 1, 3, 11, 6, 3},
    {8, 11, 1, 8, 1, 0, 11, 6, 1, 9, 1, 4, 6, 4, 1},
    {3, 11, 6, 3, 6, 0, 0, 6, 4},
    {6, 4, 8, 11, 6, 8},
    {7, 10, 6, 7, 8, 10, 8, 9, 10},
    {0, 7, 3, 0, 10, 7, 0, 9, 10, 6, 7, 10},
    {10, 6, 7, 1, 10, 7, 1, 7, 8, 1, 8, 0},
    {10, 6, 7, 10, 7, 1, 1, 7, 3},
    {1, 2, 6, 1, 6, 8, 1, 8, 9, 8, 6, 7},
    {2, 6, 9, 2, 9, 1, 6, 7, 9, 0, 9, 3, 7, 3, 9},
    {7, 8, 0, 7, 0, 6, 6, 0, 2},
    {7, 3, 2, 6, 7, 2},
    {2, 3, 11, 10, 6, 8, 10, 8, 9, 8, 6, 7},
    {2, 0, 7, 2, 7, 11, 0, 9, 7, 6, 7, 10, 9, 10, 7},
    {1, 8, 0, 1, 7, 8, 1, 10, 7, 6, 7, 10, 2, 3, 11},
    {11, 2, 1, 11, 1, 7, 10, 6, 1, 6, 7, 1},
    {8, 9, 6, 8, 6, 7, 9, 1, 6, 11, 6, 3, 1, 3, 6},
    {0, 9, 1, 11, 6, 7},
    {7, 8, 0, 7, 0, 6, 3, 11, 0, 11, 6, 0},
    {7, 11, 6},
    {7, 6, 11},
    {3, 0, 8, 11, 7, 6},
    {0, 1, 9, 11, 7, 6},
    {8, 1, 9, 8, 3, 1, 11, 7, 6},
    {10, 1, 2, 6, 11, 7},
    {1, 2, 10, 3, 0, 8, 6, 11, 7},
    {2, 9, 0, 2, 10, 9, 6, 11, 7},
    {6, 11, 7, 2, 10, 3, 10, 8, 3, 10, 9, 8},
    {7, 2, 3, 6, 2, 7},
    {7, 0, 8, 7, 6, 0, 6, 2, 0},
    {2, 7, 6, 2, 3, 7, 0, 1, 9},
    {1, 6, 2, 1, 8, 6, 1, 9, 8, 8, 7, 6},
    {10, 7, 6, 10, 1, 7, 1, 3, 7},
    {10, 7, 6, 1, 7, 10, 1, 8, 7, 1, 0, 8},
    {0, 3, 7, 0, 7, 10, 0, 10, 9, 6, 10, 7},
    {7, 6, 10, 7, 10, 8, 8, 10, 9},
    {6, 8, 4, 11, 8, 6},
    {3, 6, 11, 3, 0, 6, 0, 4, 6},
    {8, 6, 11, 8, 4, 6, 9, 0, 1},
    {9, 4, 6, 9, 6, 3, 9, 3, 1, 11, 3, 6},
    {6, 8, 4, 6, 11, 8, 2, 10, 1},
    {1, 2, 10, 3, 0, 11, 0, 6, 11, 0, 4, 6},
    {4, 11, 8, 4, 6, 11, 0, 2, 9, 2, 10, 9},
    {10, 9, 3, 10, 3, 2, 9, 4, 3, 11, 3, 6, 4, 6, 3},
    {8, 2, 3, 8, 4, 2, 4, 6, 2},
    {0, 4, 2, 4, 6, 2},
    {1, 9, 0, 2, 3, 4, 2, 4, 6, 4, 3, 8},
    {1, 9, 4, 1, 4, 2, 2, 4, 6},
    {8, 1, 3, 8, 6, 1, 8, 4, 6, 6, 10, 1},
    {10, 1, 0, 10, 0, 6, 6, 0, 4},
    {4, 6, 3, 4, 3, 8, 6, 10, 3, 0, 3, 9, 10, 9, 3},
    {10, 9, 4, 6, 10, 4},
    {4, 9, 5, 7, 6, 11},
    {0, 8, 3, 4, 9, 5, 11, 7, 6},
    {5, 0, 1, 5, 4, 0, 7, 6, 11},
    {11, 7, 6, 8, 3, 4, 3, 5, 4, 3, 1, 5},
    {9, 5, 4, 10, 1, 2, 7, 6, 11},
    {6, 11, 7, 1, 2, 10, 0, 8, 3, 4, 9, 5},
    {7, 6, 11, 5, 4, 10, 4, 2, 10, 4, 0, 2},
    {3, 4, 8, 3, 5, 4, 3, 2, 5, 10, 5, 2, 11, 7, 6},
    {7, 2, 3, 7, 6, 2, 5, 4, 9},
    {9, 5, 4, 0, 8, 6, 0, 6, 2, 6, 8, 7},
    {3, 6, 2, 3, 7, 6, 1, 5, 0, 5, 4, 0},
    {6, 2, 8, 6, 8, 7, 2, 1, 8, 4, 8, 5, 1, 5, 8},
    {9, 5, 4, 10, 1, 6, 1, 7, 6, 1, 3, 7},
    {1, 6, 10, 1, 7, 6, 1, 0, 7, 8, 7, 0, 9, 5, 4},
    {4, 0, 10, 4, 10, 5, 0, 3, 10, 6, 10, 7, 3, 7, 10},
    {7, 6, 10, 7, 10, 8, 5, 4, 10, 4, 8, 10},
    {6, 9, 5, 6, 11, 9, 11, 8, 9},
    {3, 6, 11, 0, 6, 3, 0, 5, 6, 0, 9, 5},
    {0, 11, 8, 0, 5, 11, 0, 1, 5, 5, 6, 11},
    {6, 11, 3, 6, 3, 5, 5, 3, 1},
    {1, 2, 10, 9, 5, 11, 9, 11, 8, 11, 5, 6},
    {0, 11, 3, 0, 6, 11, 0, 9, 6, 5, 6, 9, 1, 2, 10},
    {11, 8, 5, 11, 5, 6, 8, 0, 5, 10, 5, 2, 0, 2, 5},
    {6, 11, 3, 6, 3, 5, 2, 10, 3, 10, 5, 3},
    {5, 8, 9, 5, 2, 8, 5, 6, 2, 3, 8, 2},
    {9, 5, 6, 9, 6, 0, 0, 6, 2},
    {1, 5, 8, 1, 8, 0, 5, 6, 8, 3, 8, 2, 6, 2, 8},
    {1, 5, 6, 2, 1, 6},
    {1, 3, 6, 1, 6, 10, 3, 8, 6, 5, 6, 9, 8, 9, 6},
    {10, 1, 0, 10, 0, 6, 9, 5, 0, 5, 6, 0},
    {0, 3, 8, 5, 6, 10},
    {10, 5, 6},
    {11, 5, 10, 7, 5, 11},
    {11, 5, 10, 11, 7, 5, 8, 3, 0},
    {5, 11, 7, 5, 10, 11, 1, 9, 0},
    {10, 7, 5, 10, 11, 7, 9, 8, 1, 8, 3, 1},
    {11, 1, 2, 11, 7, 1, 7, 5, 1},
    {0, 8, 3, 1, 2, 7, 1, 7, 5, 7, 2, 11},
    {9, 7, 5, 9, 2, 7, 9, 0, 2, 2, 11, 7},
    {7, 5, 2, 7, 2, 11, 5, 9, 2, 3, 2, 8, 9, 8, 2},
    {2, 5, 10, 2, 3, 5, 3, 7, 5},
    {8, 2, 0, 8, 5, 2, 8, 7, 5, 10, 2, 5},
    {9, 0, 1, 5, 10, 3, 5, 3, 7, 3, 10, 2},
    {9, 8, 2, 9, 2, 1, 8, 7, 2, 10, 2, 5, 7, 5, 2},
    {1, 3, 5, 3, 7, 5},
    {0, 8, 7, 0, 7, 1, 1, 7, 5},
    {9, 0, 3, 9, 3, 5, 5, 3, 7},
    {9, 8, 7, 5, 9, 7},
    {5, 8, 4, 5, 10, 8, 10, 11, 8},
    {5, 0, 4, 5, 11, 0, 5, 10, 11, 11, 3, 0},
    {0, 1, 9, 8, 4, 10, 8, 10, 11, 10, 4, 5},
    {10, 11, 4, 10, 4, 5, 11, 3, 4, 9, 4, 1, 3, 1, 4},
    {2, 5, 1, 2, 8, 5, 2, 11, 8, 4, 5, 8},
    {0, 4, 11, 0, 11, 3, 4, 5, 11, 2, 11, 1, 5, 1, 11},
    {0, 2, 5, 0, 5, 9, 2, 11, 5, 4, 5, 8, 11, 8, 5},
    {9, 4, 5, 2, 11, 3},
    {2, 5, 10, 3, 5, 2, 3, 4, 5, 3, 8, 4},
    {5, 10, 2, 5, 2, 4, 4, 2, 0},
    {3, 10, 2, 3, 5, 10, 3, 8, 5, 4, 5, 8, 0, 1, 9},
    {5, 10, 2, 5, 2, 4, 1, 9, 2, 9, 4, 2},
    {8, 4, 5, 8, 5, 3, 3, 5, 1},
    {0, 4, 5, 1, 0, 5},
    {8, 4, 5, 8, 5, 3, 9, 0, 5, 0, 3, 5},
    {9, 4, 5},
    {4, 11, 7, 4, 9, 11, 9, 10, 11},
    {0, 8, 3, 4, 9, 7, 9, 11, 7, 9, 10, 11},
    {1, 10, 11, 1, 11, 4, 1, 4, 0, 7, 4, 11},
    {3, 1, 4, 3, 4, 8, 1, 10, 4, 7, 4, 11, 10, 11, 4},
    {4, 11, 7, 9, 11, 4, 9, 2, 11, 9, 1, 2},
    {9, 7, 4, 9, 11, 7, 9, 1, 11, 2, 11, 1, 0, 8, 3},
    {11, 7, 4, 11, 4, 2, 2, 4, 0},
    {11, 7, 4, 11, 4, 2, 8, 3, 4, 3, 2, 4},
    {2, 9, 10, 2, 7, 9, 2, 3, 7, 7, 4, 9},
    {9, 10, 7, 9, 7, 4, 10, 2, 7, 8, 7, 0, 2, 0, 7},
    {3, 7, 10, 3, 10, 2, 7, 4, 10, 1, 10, 0, 4, 0, 10},
    {1, 10, 2, 8, 7, 4},
    {4, 9, 1, 4, 1, 7, 7, 1, 3},
    {4, 9, 1, 4, 1, 7, 0, 8, 1, 8, 7, 1},
    {4, 0, 3, 7, 4, 3},
    {4, 8, 7},
    {9, 10, 8, 10, 11, 8},
    {3, 0, 9, 3, 9, 11, 11, 9, 10},
    {0, 1, 10, 0, 10, 8, 8, 10, 11},
    {3, 1, 10, 11, 3, 10},
    {1, 2, 11, 1, 11, 9, 9, 11, 8},
    {3, 0, 9, 3, 9, 11, 1, 2, 9, 2, 11, 9},
    {0, 2, 11, 8, 0, 11},
    {3, 2, 11},
    {2, 3, 8, 2, 8, 10, 10, 8, 9},
    {9, 10, 2, 0, 9, 2},
    {2, 3, 8, 2, 8, 10, 0, 1, 8, 1, 10, 8},
    {1, 10, 2},
    {1, 3, 8, 9, 1, 8},
    {0, 9, 1},
    {0, 3, 8}    
  };
  
  static Vector CUBE_VERTS_TABLE[] = {
    new Vector(0,0,0,1),
    new Vector(1,0,0),
    new Vector(1,1,0),
    new Vector(0,1,0),
    new Vector(0,0,1),
    new Vector(1,0,1),
    new Vector(1,1,1),
    new Vector(0,1,1)
  };
  
  static byte EDGE_INDEX[][] = {
    {0,1}, {1,2}, {2,3}, {3,0}, {4,5}, {5,6}, {6,7}, {7,4}, {0,4}, {1,5}, {2,6}, {3,7}    
  };
  
  interface DensityMap {
    double density(Vector v);
  }
  
  class SphereDensity implements DensityMap {
    
    Vector center;
    double radius;

    SphereDensity(Vector center, double radius) {
      this.center = center;
      this.radius = radius;
    }

    @Override
    public double density(Vector v) {
      return v.minus(center).length() - radius;
    }
    
  }
  
  public CubeMap(Context context, int size, double scale) {
    this.context = context;
    this.densityMaps = Lists.newArrayList();
    this.scale = scale;
    this.mx = size;
    this.my = size;
    this.mz = size;
    this.dimScale = new Vector(1.0/mx, 1.0/my, 1.0/mz, 1.0);
    densityMaps.add(new SphereDensity(new Vector(size/2.0, size/2.0, size/2.0, 1), size/4));
    genCloud();
    cloud.freeze();
    program = new ProgramRenderer(context, cloud, "screen");
  }
  
  public int getIndex(Vector p) {
    return (int)((p.get(0)*mx + p.get(1))*my + p.get(2));
  }

  
  public void genCloud() {
    cloud = new VertexCloud();
    double grid[] = new double[CUBE_VERTS_TABLE.length];
    Vector ps[] = new Vector[CUBE_VERTS_TABLE.length];
    Vector vs[] = new Vector[32];
    
    for(int x=0;x<mx;++x) {
      for(int y=0;y<my;++y) {
        for(int z=0;z<mz;++z) {
          
          Vector p = new Vector(x, y, z, 1);
          int cubeIndex = 0;
          for(int i=0;i<CUBE_VERTS_TABLE.length;++i) {
            ps[i] = p.plus(CUBE_VERTS_TABLE[i]);
            grid[i] = getDensity(ps[i]);
            if ( grid[i] > 0 ) {
              cubeIndex |= (0x1 << i);
            }
          }
          
          long edgeMask = EDGE_TABLE[cubeIndex];
          if ( edgeMask != 0 ) {
            for(int i=0;i<12;++i) {
              if ( (edgeMask & (0x1 << i)) > 0 ) {
                byte e1 = EDGE_INDEX[i][0];
                byte e2 = EDGE_INDEX[i][1];
                vs[i] = midPoint(grid[e1], grid[e2], ps[e1], ps[e2]);
              }
            }
            
            byte[] triIndexes = TRI_TABLE[cubeIndex];
            for(int i=0;i<triIndexes.length;i++) {
              cloud.addVertex(vs[triIndexes[i]].times(scale), Vector.ZERO, Vector.ZERO);
            }
          }
        }
      }
    }
    cloud.computeNormals();
  }
  
  private Vector midPoint(double d1, double d2, Vector p1, Vector p2) {
    return p1.plus(p2.minus(p1).times(d1 / (d1 - d2)));
  }

  // change this code to move edge points for edges
  // that inersect
  public VertexCloud oldGenCloud() {
    cloud = new VertexCloud();
    Vector dx = new Vector(1, 0, 0, 1);
    Vector dy = new Vector(0, 1, 0, 1);
    Vector dz = new Vector(0, 0, 1, 1);
    Vector hdx = dx.times(0.5);
    Vector hdy = dy.times(0.5);
    Vector hdz = dz.times(0.5);
    Vector mdx = dx.times(-0.5);
    Vector mdy = dy.times(-0.5);
    Vector mdz = dz.times(-0.5);

    Map<Vector, Vector> dp = Maps.newHashMap();
    for(int x=0;x<mx;++x) {
      for(int y=0;y<my;++y) {
        for(int z=0;z<mz;++z) {
          Vector p = new Vector(x, y, z, 1);
          if ( crossing(p, p.plus(dx) ) ) {
            addFace(dp, p.plus(hdx), hdy, hdz, hdx);
          }
          if ( crossing(p, p.plus(dy) ) ) {
            addFace(dp, p.plus(hdy), hdx, hdz, hdy);
          }
          if ( crossing(p, p.plus(dz) ) ) {
            addFace(dp, p.plus(hdz), hdx, hdy, hdz);
          }
        }
      }
    }
          
    return cloud;
  }
  
  private void addEdge(Vector p, Vector hdz, Vector hdx, Vector hdy) {
    Vector p1 = p.plus(hdy).plus(hdz);
    Vector p2 = p.plus(hdx).plus(hdz);
    Vector p3 = p.plus(hdx).plus(hdy);

    addTriangle(cloud, p1.times(scale), p2.times(scale), p3.times(scale));
  }

  private void addEdges(Vector p, Vector hdx, Vector hdy, Vector hdz) {
    
    if ( true ) {
      Vector p1 = p.plus(hdx).minus(hdy);
      Vector p2 = p.plus(hdx).minus(hdz);
      Vector p3 = p.plus(hdx).plus(hdy);
      Vector p4 = p.plus(hdx).plus(hdz);
      
      addTriangle(cloud, p1.times(scale), p2.times(scale), p3.times(scale));
      addTriangle(cloud, p1.times(scale), p3.times(scale), p4.times(scale));
    } else {
      Vector p1 = p.plus(hdx).minus(hdy).minus(hdz);
      Vector p2 = p.plus(hdx).plus(hdy).minus(hdz);
      Vector p3 = p.plus(hdx).plus(hdy).plus(hdz);
      Vector p4 = p.plus(hdx).minus(hdy).plus(hdz);

      addTriangle(cloud, p1.times(scale), p2.times(scale), p3.times(scale));
      addTriangle(cloud, p1.times(scale), p3.times(scale), p4.times(scale));
    }

  }

  private boolean crossing(Vector p, Vector q) {
    return (getDensity(p) > 0) != (getDensity(q) > 0);
  }
  
  
  private void addEdges(Relation<Vector, Edge> edgeRelation, Vector p, Vector dp) {
    double d1 = getDensity(p);
    double d2 = getDensity(p.plus(dp));
    if ( (d1 > 0) != (d2 > 0) ) {
      double s = Math.abs(d1/(d2- d1));
      Edge edge = new Edge();
      edge.p1 = p;
      edge.p2 = p.plus(dp);
      edge.p3 = p.plus(dp.times(s));
      edgeRelation.put(edge.p1, edge);
      edgeRelation.put(edge.p2, edge);
    }
  }

  static class Edge {
    Vector p1, p2, p3;

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((p1 == null) ? 0 : p1.hashCode());
      result = prime * result + ((p2 == null) ? 0 : p2.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      Edge other = (Edge) obj;
      if (p1 == null) {
        if (other.p1 != null)
          return false;
      } else if (!p1.equals(other.p1))
        return false;
      if (p2 == null) {
        if (other.p2 != null)
          return false;
      } else if (!p2.equals(other.p2))
        return false;
      return true;
    }
  }
  
  
  
  private void addFace(Map<Vector, Vector> dp, Vector p, Vector dx, Vector dy, Vector dz) {
    
    Vector p1 = getVertex(dp, p.minus(dx).minus(dy), dx, dy, dz);
    Vector p2 = getVertex(dp, p.minus(dx).plus(dy), dx, dy, dz);
    Vector p3 = getVertex(dp, p.plus(dx).plus(dy), dx, dy, dz);
    Vector p4 = getVertex(dp, p.plus(dx).minus(dy), dx, dy, dz);

    Vector n1 = p2.minus(p1).cross(p3.minus(p1));
    cloud.addVertex(p1.times(scale), n1, Vector.ZERO);
    cloud.addVertex(p2.times(scale), p1.cross(p3).normalize(), Vector.ZERO);
    cloud.addVertex(p3.times(scale), p1.cross(p2).normalize(), Vector.ZERO);
    
    Vector n2 = p3.minus(p1).cross(p4.minus(p1));
    cloud.addVertex(p1.times(scale), n2, Vector.ZERO);
    cloud.addVertex(p3.times(scale), n2, Vector.ZERO);
    cloud.addVertex(p4.times(scale), n2, Vector.ZERO);
  }

  private Vector getVertex(Map<Vector, Vector> dp, Vector p, Vector dx, Vector dy, Vector dz) {
    if (true) return p;
    Vector p1 = dp.get(p);
    if ( p1 == null ) {
      p1 = p;
      p1 = p1.plus(displace(p, dx).times(0.125));
      p1 = p1.plus(displace(p, dy).times(0.125));
      p1 = p1.plus(displace(p, dz).times(0.125));
      p1 = p1.plus(displace(p, dx.times(-1)).times(0.125));
      p1 = p1.plus(displace(p, dy.times(-1)).times(0.125));
      p1 = p1.plus(displace(p, dz.times(-1)).times(0.125));
      dp.put(p, p1);
    }
    return p1;
  }

  private Vector displace(Vector p, Vector dx) {
    double d1 = getDensity(p);
    double d2 = getDensity(p.plus(dx));
    if ( (d1 > 0) != (d2 < 0) ) {
      double phi = d1 / (d1 - d2);
      if ( phi < 0.5 ) {
        return dx.times(phi);
      }
    }
    return Vector.ZERO;
  }

  private Vector getMidpoint(Vector p1, Vector p2) {
    double d1 = getDensity(p1);
    double d2 = getDensity(p2);
    if ( d1 > 0 != d2 > 0 ) {
      double phi = d1 / (d1 - d2);
      if ( phi < 0.5 ) {
        return p1.plus(p2.minus(p1).times(phi));
      }
    } 
    return p1;
  }

  private void addBorder(Set<Vector> ns, Vector p, Set<Vector> border) {
    if ( border.contains(p) ) {
      ns.add(p);
    }
  }

  private void addTriangle(VertexCloud cloud, Vector p1, Vector p2, Vector p3) {
    Vector n = p2.minus(p1).cross(p3.minus(p1)).normalize();
    addVertex(cloud, n, p1);
    addVertex(cloud, n, p2);
    addVertex(cloud, n, p3);
  }

  private void addVertex(VertexCloud cloud, Vector n, Vector p) {
    cloud.addVertex(p.times(scale), n, p.times(scale).elementTimes(dimScale));
  }

  private void addFaces(Vector p, Vector dp, Vector dp1, Vector dp2) {
    double d1 = getDensity(p);
    double d2 = getDensity(p.plus(dp));
    if ( (d1 > 0) != (d2 > 0) ) {
      Vector pdp  = dp.times(0.5);
      Vector pdp1 = dp1.times(0.5);
      Vector pdp2 = dp2.times(0.5);
      Vector mdp1 = dp1.times(-0.5);
      Vector mdp2 = dp2.times(-0.5);
      addVector(cloud, p.plus(pdp).plus(mdp1).plus(mdp2), dp, new Vector(0, 0, 0, 1));
      addVector(cloud, p.plus(pdp).plus(pdp1).plus(mdp2), dp, new Vector(1, 0, 0, 1));
      addVector(cloud, p.plus(pdp).plus(pdp1).plus(pdp2), dp, new Vector(1, 1, 0, 1));

      addVector(cloud, p.plus(pdp).plus(pdp1).plus(pdp2), dp, new Vector(1, 1, 0, 1));
      addVector(cloud, p.plus(pdp).plus(mdp1).plus(pdp2), dp, new Vector(0, 1, 0, 1));
      addVector(cloud, p.plus(pdp).plus(mdp1).plus(mdp2), dp, new Vector(0, 0, 0, 1));
    }
  }
  
  public void addVector(VertexCloud cloud, Vector p, Vector n, Vector t) {
    cloud.addVertex(p.times(scale), n, t);
  }

  public double getDensity(Vector p) {
    double density = 0;
    for(DensityMap d: densityMaps) {
      density += d.density(p);
    }
    return density;
  }
  
  @Override
  public void render() {
    program.render();
  }

  @Override
  public void register() {
    context.getScene().register(this);
  }

}
