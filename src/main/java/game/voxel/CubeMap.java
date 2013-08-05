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
  
  VertexCloud cloud;
  Context context;
  ProgramRenderer program;
  
  public CubeMap(Context context, Vector bottomLeft, Vector topRight, Transform transform, DensityFunction densityFunction, Tessellation ts) {
    this.context = context;
    this.cloud = new VertexCloud();
    ts.genCloud(cloud, bottomLeft, topRight, densityFunction, transform);
    cloud.freeze();
    program = new ProgramRenderer(context, cloud, "screen");
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
