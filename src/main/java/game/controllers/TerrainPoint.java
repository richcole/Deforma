package game.controllers;

import game.DensityProvider;

public class TerrainPoint implements DensityProvider {
  
  private double d = 1.0;

  public TerrainPoint(double d) {
    this.d = d;
  }

  @Override
  public double getDensity() {
    return d;
  }

}
