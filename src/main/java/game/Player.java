package game;

import game.base.SimObject;
import game.enums.TileSet;
import game.math.Matrix;
import game.math.Vector;
import game.models.Creature;
import game.models.Grid.TileSquare;
import game.models.LittleLight;
import game.models.TerrainTile;
import game.nwn.readers.set.SetReader.TileSetDescription;

import org.lwjgl.util.glu.GLU;

public class Player implements SimObject {
  
  static Vector LEFT     = Vector.LEFT;
  static Vector UP       = Vector.UP;
  static Vector NORMAL   = Vector.NORMAL;

  Vector pos;
  Vector left;
  Vector up;
  Vector normal;
  Vector velocity;

  double theta1, theta2;
  
  boolean movingDownward = false;
  boolean movingUpward = false;
  boolean movingForward = false;
  boolean movingBackward = false;
  boolean movingLeft = false;
  boolean movingRight = false;

  private Context context;
  private Creature selectedCreature;
  private TileSquare selectedTileSquare;
  private TileSetDescription tileSetDescription;
  
  public Player(Context context) {
    this.context = context;
    this.pos = new Vector(-100, -100, 150);
    this.theta1 = 45;
    this.theta2 = -45;
    this.tileSetDescription = context.getTileSetDescriptions().getTileSetDescription(TileSet.Tin01);
    this.selectedTileSquare = context.getTerrain().getTileSquare(0, 0);
  }

  Vector getLeft() {
    return left;
  }
  
  Vector getUp() {
    return up;
  }

  Vector getNormal() {
    return normal;
  }

  public synchronized void render() {
    Matrix rotUp = Matrix.rot(-theta1, UP);
    left = rotUp.times(LEFT);
    normal = rotUp.times(NORMAL);
    
    Matrix rotLeft = Matrix.rot(-theta2, left);
    normal = rotLeft.times(normal);
    up = rotLeft.times(UP);
    
    Vector p = pos.plus(velocity.times(context.getSimulator().getCurrentTickNibble()));
    Vector a = p.plus(getNormal());
    Vector u = getUp();
    GLU.gluLookAt((float)p.x(), (float)p.y(), (float)p.z(), (float)a.x(), (float)a.y(), (float)a.z(), (float)u.x(), (float)u.y(), (float)u.z());
    context.getSelectionRay().updateViewMatrix();
  }
  
  @Override
  public void tick() {
    velocity = Vector.ZERO;
    if ( movingForward ) {
      velocity = velocity.plus(getNormal());
    }
    if ( movingBackward ) {
      velocity = velocity.plus(getNormal().times(-1));
    }
    if ( movingLeft ) {
      velocity = velocity.plus(getLeft());
    }
    if ( movingRight ) {
      velocity = velocity.plus(getLeft().times(-1));
    }
    if ( movingUpward ) {
      velocity = velocity.plus(getUp());
    }
    if ( movingDownward ) {
      velocity = velocity.plus(getUp().times(-1));
    }
    float velocityScale = 2;
    velocity.scaleTo(velocityScale);
    pos = pos.plus(velocity);
  }

  public void setMovingDownward(boolean movingForward) {
    this.movingDownward = movingForward;
  }

  public void setMovingUpward(boolean movingForward) {
    this.movingUpward = movingForward;
  }

  public void setMovingForward(boolean movingForward) {
    this.movingForward = movingForward;
  }

  public void setMovingBackward(boolean movingBackward) {
    this.movingBackward = movingBackward;
  }

  public void setMovingLeft(boolean movingLeft) {
    this.movingLeft = movingLeft;
  }

  public void setMovingRight(boolean movingRight) {
    this.movingRight = movingRight;
  }

  public void fire() {
    Creature creature = context.newCreature();
    creature.register();
  }

  public void fireLight() {
    LittleLight littleLight = new LittleLight(context, getNormal().scaleTo(20), pos.plus(getNormal().scaleTo(10)));
    littleLight.register();
  }

  @Override
  public Vector getPos() {
    return pos;
  }

  @Override
  public double getMass() {
    return 100;
  }

  public void register() {
    context.getSimulator().register(this);
  }

  public void setSelectedCreature(Creature selectedCreature) {
    if ( this.selectedCreature != null ) {
      this.selectedCreature.setSelected(false);
    }
    if ( selectedCreature != null ) {
      selectedCreature.setSelected(true);
    }
    this.selectedCreature = selectedCreature;
  }

  public Creature getSelectedCreature() {
    return selectedCreature;
  }
  
  public void nextTerrainTileIndex() {
    updateSelectedTile(1);
  }
  
  public void prevTerrainTileIndex() {
    updateSelectedTile(-1);
  }

  private void updateSelectedTile(int dirn) {
    if ( selectedTileSquare != null ) {
      TerrainTile tile = selectedTileSquare.getTerrainTile();
      if ( tile == null ) {
        tile = context.newTile();
        selectedTileSquare.setTerrainTile(tile);
        tile.setModel(tileSetDescription.getTiles().get(0).getModel());
        tile.setModelIndex(0);
      } else {
        int numTiles = tileSetDescription.getTiles().size();
        int modelIndex = tile.getModelIndex() + dirn;
        if ( modelIndex < 0 ) {
          modelIndex = numTiles + modelIndex;
        } else if ( modelIndex >= numTiles ) {
          modelIndex = modelIndex - numTiles;
        }
        tile.setModel(tileSetDescription.getTiles().get(modelIndex).getModel());
        tile.setModelIndex(modelIndex);
      }
    }
  }

  public void setSelectedTileSquare(TileSquare selectedTileSquare) {
    this.selectedTileSquare = selectedTileSquare;
  }
  
  public void rotate(double dtheta1, double dtheta2) {
    theta1 += dtheta1 * 6.283f / 5000.0f;
    theta2 += dtheta2 * 6.283f / 5000.0f;
  }
}
