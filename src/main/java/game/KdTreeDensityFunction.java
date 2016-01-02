package game;

public class KdTreeDensityFunction<T extends DensityProvider> implements DensityFunction {
  
  private KdTree<T> tree;

  public KdTreeDensityFunction(KdTree<T> tree) {
    this.tree = tree;
  }

  @Override
  public double getDensity(Vector p) {
    KdTree.NN<T> nn = tree.nn(p);
    
    if ( nn == null || nn.getV1() == null ) {
      return 1.0;
    }

    if ( nn.getV2() == null ) {
      return nn.getT1().getDensity();
    }
    
    double den1 = nn.getT1().getDensity();
    double den2 = nn.getT2().getDensity();
    double d1 = p.dist(nn.getV1());
    double d2 = p.dist(nn.getV2());
    double alpha1 = d1 / (d1 + d2);
    double alpha2 = d2 / (d1 + d2);
    return alpha2 * den1 + alpha1 * den2;
  }

  @Override
  public Vector getDensityDerivative(Vector p) {
    KdTree.NN<T> nn = tree.nn(p);
    return nn.getV1().minus(nn.getV2()).normalize();
  }

  @Override
  public boolean isPositive(double d) {
    return d > 0;
  }

}
