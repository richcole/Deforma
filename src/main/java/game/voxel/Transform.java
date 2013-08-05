package game.voxel;

import game.math.Vector;

public interface Transform {

  Vector transform(Vector vector);

  Vector transformNormal(Vector q1, Vector n1);

}
