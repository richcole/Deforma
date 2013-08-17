package game.voxel;

import game.Context;
import game.Registerable;
import game.Renderable;
import game.math.Vector;
import game.proc.VertexCloud;
import game.shaders.ProgramRenderer;

public class OctTreeTest implements Renderable, Registerable {

  private Context context;
  private double radius;
  private Vector center;
  private OctTree octTree;
  private PotentialField s1;
  private VertexCloud cloud;
  private ProgramRenderer program;

  public OctTreeTest(Context context) {
    super();
    this.context = context;
    this.radius = 50;
    this.center = Vector.ONES.times(radius);
    this.octTree = new OctTree(center, radius, 6, 2);
    this.s1 = new PotentialField(center, radius/2);
    this.octTree.add(s1);
    this.cloud = new VertexCloud();

    ScaleTransform transform = new ScaleTransform(400/radius);

    octTree.renderToVertexCloud(cloud, transform, s1);
    cloud.freeze();

    this.program = new ProgramRenderer(context, cloud, "screen"); 
  }
  
  public void register() {
    context.getScene().register(this);
  }

  @Override
  public void render() {
    program.render();
  }

  
}
