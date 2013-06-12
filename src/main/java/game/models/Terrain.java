package game.models;

import game.Context;
import game.Renderable;
import game.math.Vector;

public class Terrain implements Renderable {

  int dx;
  int dy;
  double size;
  Tile[] tiles;
  Context context;
  
  Rect rect;
  
  static class Tile {

    private int x;
    private int y;

    public Tile(int x, int y) {
      this.x = x;
      this.y = y;
    }
    
  }
  
  public Terrain(Context context, int dx, int dy, double size) {
    this.context = context;
    this.dx = dx;
    this.dy = dy;
    this.size = size;
    this.rect = new Rect(Vector.ZERO, Vector.NORMAL.times(size/2), Vector.LEFT.times(size/2), context.getStoneTexture());
    this.tiles = new Tile[dx*dy];
    for(int j=0;j<dx; ++j) {
      for(int i=0;i<dx; ++i) {
        tiles[index(i, j)] = new Tile(i, j);
      }
    }
  }

  private int index(int i, int j) {
    return j*dx + i;
  }

  @Override
  public void render() {
    for(Tile tile: tiles) {
      rect.setPos(new Vector(tile.x*size, tile.y*size, 0, 1.0));
      rect.render();
    }
  }
  
}
