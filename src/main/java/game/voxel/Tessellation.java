package game.voxel;

import game.math.Vector;
import game.proc.VertexCloud;

public interface Tessellation {
  public void genCloud(VertexCloud cloud, Vector bottomLeft, Vector topRight, DensityFunction densityFunction, Transform transformation);
}
