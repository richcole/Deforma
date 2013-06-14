package game.models;

import game.Context;
import game.Renderable;
import game.base.Face;
import game.base.SimObject;
import game.base.Texture;
import game.base.io.Serializer;
import game.math.MathUtils;
import game.math.Vector;
import game.models.Terrain.Tile;
import game.nwn.NwnMesh;
import game.nwn.NwnTextureProvider;
import game.nwn.readers.MdlModel;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.apache.log4j.Logger;
import org.lwjgl.opengl.GL11;

public class Creature implements Renderable, SimObject {
  
  private static Logger logger = Logger.getLogger(Creature.class);
    
  Context context;
  NwnMesh mesh;
  double scale;
  long tick = 0;
  Vector pos = Vector.ZERO;
  Vector velocity = Vector.NORMAL;
  double mass = 0;
  double alpha = 0;
  
  Vector dest;
  Tile tile;
  Creature target;
  boolean selected;
  
  private String animName;
  private AnimMesh animMesh;
  private String modelName;
  
  enum State {
    WAITING("cpause1"),
    TRAVELLING("cwalk"),
    ATTACKING("ca1stab");
    
    String animName;
    
    State(String animName) {
      this.animName = animName;
    }
    
    String getAnimName() {
      return animName;
    }
  }
  
  State state = State.WAITING;

  public Creature(Context context, String modelName, String animName, double scale) {
    this.context = context;
    this.modelName = modelName;
    Serializer serializer = new Serializer();
    this.animMesh = serializer.deserialize(new File("res/wererat.mdl.gz"), AnimMesh.class);
    this.scale = scale;
    this.animName = animName;
  }

  @Override
  public void render() {
    AnimMeshRenderer renderer = new AnimMeshRenderer(context);
    GL11.glPushMatrix();
    GL11.glTranslated(pos.x(), pos.y(), pos.z());
    GL11.glScaled(scale, scale, scale);
    double theta = MathUtils.toDegrees(velocity.theta(Vector.NORMAL, Vector.LEFT));
    GL11.glRotated(theta, 0, 0, 1d);
    renderer.render(animMesh, state.getAnimName(), alpha);
    if ( selected ) {
      renderSelection();
    }
    GL11.glPopMatrix();
  }
  
  @Override
  public void tick() {
    tick = tick + 1;
    incrementAlpha();
    Terrain terrain = context.getTerrain();
    if ( tile == null ) {
      tile = terrain.addCreature(this, pos);
      pos = terrain.center(tile);
    }
    if ( state == State.TRAVELLING ) {
      velocity = dest.minus(pos).scaleTo(scale / 500f);
      Vector newPos = pos.plus(velocity);
      Tile newTile = terrain.tileAt(newPos);
      if ( newTile == tile ) {
        pos = newPos;
      } else if (newTile.creature == null) {
        terrain.moveCreature(this, tile, newTile);
        tile = newTile;
        pos = newPos;
      }
      if ( nearPoint(dest) ) {
        state = State.WAITING;
        alpha = 0;
      } 
    } else if ( state == State.WAITING ) {
      Random random = new Random();
      if ( random.nextInt(1000) <= 1 ) {
        int x = random.nextInt(terrain.dx);
        int y = random.nextInt(terrain.dy);
        dest = terrain.center(x, y); 
        alpha = 0;
        state = State.TRAVELLING;
      }
    } 
    if ( target == null ) {
      for(Tile otherTile: terrain.neighbourhood(tile, 1)) {
        if ( otherTile.creature != null ) {
          target = otherTile.creature;
        }
      }
    }
    if ( target != null ) {
      if ( nearCenterOfTile(terrain) ) {
        state = State.ATTACKING;
        velocity = target.getPos().minus(pos).scaleTo(scale / 500f);
      } else {
        state = State.TRAVELLING;
        dest = terrain.center(tile);
      }
    }
  }

  private boolean nearCenterOfTile(Terrain terrain) {
    return nearPoint(terrain.center(tile));
  }
  
  private boolean nearPoint(Vector p) {
    return p.minus(pos).length() < 2;
  }

  private void incrementAlpha() {
    alpha = (alpha + 0.001) % 1.0f;
  }

  @Override
  public Vector getPos() {
    return pos;
  }

  @Override
  public double getMass() {
    return mass;
  }

  public void register() {
    context.getSimulator().register(this);
    context.getScene().register(this);
  }

  public void renderSelection() {
    GL11.glBegin(GL11.GL_TRIANGLES);
    GL11.glColor3f(1, 1, 1);
    GL11.glVertex3d(0, 1, 0.1);
    GL11.glVertex3d(-1, -1, 0.1);
    GL11.glVertex3d(1, -1, 0.1);
    GL11.glEnd();
  }

  public boolean getSelected() {
    return selected;
  }

  public void setSelected(boolean selected) {
    this.selected = selected;
  }
  
  
}
