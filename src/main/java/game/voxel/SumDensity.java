package game.voxel;

import java.util.List;

import com.google.common.collect.Lists;

import game.math.Vector;

public class SumDensity implements DensityFunction {
  
  List<DensityFunction> fs;

  public SumDensity(DensityFunction ... fs) {
    this.fs = Lists.newArrayList(fs);
  }

  @Override
  public double getDensity(Vector v) {
    Double p = null;
    Double n = null;
    for(DensityFunction f: fs) {
      double d = f.getDensity(v);
      if ( d >= 0 && (p == null || Math.abs(d) < Math.abs(p)) ) {
        p = d;
      }
      if ( d < 0 && (n == null || Math.abs(d) < Math.abs(n)) ) {
        n = d;
      }
    }
    return p != null ? p : n;
  }


  @Override
  public Vector getDensityDerivative(Vector v) {
    Double p = null;
    Double n = null;
    Vector dp = null;
    Vector dn = null;
    for(DensityFunction f: fs) {
      double d = f.getDensity(v);
      if ( d >= 0 && (p == null || Math.abs(d) < Math.abs(p)) ) {
        p = d;
        dp = f.getDensityDerivative(v);
      }
      if ( d < 0 && (n == null || Math.abs(d) < Math.abs(n)) ) {
        n = d;
        dn = f.getDensityDerivative(v);
      }
    }
    return p != null ? dp : dn;
  }
  
}