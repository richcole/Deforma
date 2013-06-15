package game.models;

import game.Context;
import game.Renderable;
import game.math.Vector;

import java.util.List;

import com.google.common.collect.Lists;

public class Terrain implements Renderable {

  int dx;
  int dy;
  double size;
  Tile[] tiles;
  Context context;
  
  Rect rect;
  
  public static class Tile {

    public int x;
    public int y;
    public Creature creature;

    public Tile(int x, int y) {
      this.x = x;
      this.y = y;
    }

    public Creature getCreature() {
      return creature;
    }

    public int getX() {
      return x;
    }

    public int getY() {
      return y;
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
  
  public Vector center(int x, int y) {
    Tile tile = tiles[index(x,y)];
    return center(tile);
  }

  public Vector center(Tile tile) {
    return new Vector((tile.x)*size, (tile.y)*size, 0, 1.0);
  }

  public void register() {
    context.getScene().register(this);
  }

  public Tile addCreature(Creature creature, Vector pos) {
    Tile tile = tileAt(pos);
    for(int i=0;i<10;++i) {
      for(Tile cand: neighbourhood(tile, i)) {
        if ( cand.creature == null ) {
          cand.creature = creature;
          return cand;
        }
      }
    }
    return null;
  }

  public Tile tileAt(Vector pos) {
    int x = (int) Math.floor((pos.x() + (size*0.5)) / size);
    int y = (int) Math.floor((pos.y() + (size*0.5)) / size);
    Tile tile = tiles[index(x,y)];
    return tile;
  }
  
  List<Tile> neighbourhood(Tile tile, int i) {
    List<Tile> r = Lists.newArrayList();
    if ( i == 0 ) {
      r.add(tile); 
    } else {
      for(int sx=-i; sx<=i; ++sx) {
        for(int sy=-i; sy<=i; sy+=i*2) {
          addTile(r, tile, sx, sy);
        }      
      }
      for(int sy=-i+1; sy<i; ++sy) {
        for(int sx=-i; sx<=i; sx+=i*2) {
          addTile(r, tile, sx, sy);
        }      
      }
    }
    return r;
  }

  private void addTile(List<Tile> r, Tile tile, int sx, int sy) {
    int ax = tile.x+sx;
    int ay = tile.y+sy;
    if ( ax >= 0 && ax < dx && ay >= 0 && ay < dy ) {
      r.add(tiles[index(ax, ay)]);
    }
  }

  public void moveCreature(Creature creature, Tile oldTile, Tile newTile) {
    oldTile.creature = null;
    newTile.creature = creature;
  }

}
