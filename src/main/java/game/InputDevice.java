package game;

import game.math.Vector;
import game.models.Grid.GridSquare;
import game.models.Grid.TileSquare;

import org.apache.log4j.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InputDevice {
  
  private static Logger logger = Logger.getLogger(InputDevice.class);
  
  float mx = 0, my = 0;
  float x = 0, y = 0;
  boolean haveMouseCoords = false;
  boolean quit = false;
  boolean grabbed = false;

  private Context context;

  public InputDevice(Context context) {
    this.context = context;
    Mouse.setGrabbed(grabbed);
  }

  public boolean process() {
    boolean eventProcessed = false;
    if ( Display.isCloseRequested() ) {
      quit = true;
      eventProcessed = true;
    }
    eventProcessed = processMouse() || eventProcessed;
    eventProcessed = processKeyboard() || eventProcessed;
    return eventProcessed;
  }

  private boolean processKeyboard() {
    boolean eventProcessed = false;
    while( Keyboard.next() ) {
      int key = Keyboard.getEventKey();
      boolean pressed = Keyboard.getEventKeyState();
      
      switch(key) {
      case Keyboard.KEY_RETURN:
        if ( pressed ) {
        }
        break;
      case Keyboard.KEY_ESCAPE:
        quit = true;
        break;
      case Keyboard.KEY_Q:
        context.getPlayer().setMovingUpward(pressed);
        break;
      case Keyboard.KEY_E:
        context.getPlayer().setMovingDownward(pressed);
        break;
      case Keyboard.KEY_W:
        context.getPlayer().setMovingForward(pressed);
        break;
      case Keyboard.KEY_S:
        context.getPlayer().setMovingBackward(pressed);
        break;
      case Keyboard.KEY_A:
        context.getPlayer().setMovingLeft(pressed);
        break;
      case Keyboard.KEY_D:
        context.getPlayer().setMovingRight(pressed);
        break;
      case Keyboard.KEY_SPACE:
        if ( pressed ) {
          context.getPlayer().fire();
        }
        break;
      case Keyboard.KEY_L:
        if ( pressed ) {
          context.getPlayer().fireLight();
        }
        break;
      }
      eventProcessed = true;
    }
    return eventProcessed;
  }

  private boolean processMouse() {
    boolean eventProcessed = false;
    while( Mouse.next() ) {
      processRightClick();
      processWheel();
      if ( ! grabbed ) {
        boolean pressed = Mouse.getEventButtonState();
        if ( pressed ) {
          int button = Mouse.getEventButton();
          switch( button ) {
          case 0:
            selectTerrainCreature(Mouse.getX(), Mouse.getY());
            break;
          }
        }
      }
      else {
        if ( haveMouseCoords ) {
          float dx = Mouse.getX() - mx;
          float dy = Mouse.getY() - my;
          x += dx;
          y += dy;
          context.getPlayer().rotate(dx, dy);
        }
        haveMouseCoords = true;
        mx = Mouse.getX();
        my = Mouse.getY();
        float minX = context.getView().getWidth() / 5;
        float minY = context.getView().getHeight() / 5;
        float maxX = context.getView().getWidth() - minX;
        float maxY = context.getView().getHeight() - minY;
        if ( mx <= minX || mx >= maxX || my <= minY || my >= maxY ) {
          haveMouseCoords = false;
          Mouse.setCursorPosition((int)context.getView().getWidth()/2, (int)context.getView().getHeight()/2);
        }
        eventProcessed = true;
      }
    }
    return eventProcessed;
  }

  private void processRightClick() {
    if ( Mouse.getEventButton() == 1 ) {
      grabbed = Mouse.getEventButtonState();
      Mouse.setGrabbed(grabbed);
      haveMouseCoords = false;
    }
  }

  private void processWheel() {
    if ( Mouse.getEventDWheel() > 0 ) {
      context.getPlayer().nextTerrainTileIndex();
    } else if ( Mouse.getEventDWheel() < 0 ) {
      context.getPlayer().prevTerrainTileIndex();
    }
  }
  
  private void selectTerrainCreature(int x2, int y2) {
    Vector f = context.getSelectionRay().getSelectionRay((float)x2, (float)y2);
    Player player = context.getPlayer();
    Vector p = player.getPos();
    double s = -p.z() / f.z();
    Vector x = p.plus(f.times(s));
    GridSquare tile = context.getTerrain().getGridSquareAt(x);
    if ( tile  != null ) {
      player.setSelectedCreature(tile.getCreature());
    } else {
      player.setSelectedCreature(null);
    }
    
    TileSquare tileSquare = context.getTerrain().getTileSquareAt(x);
    if ( tileSquare != null ) {
      player.setSelectedTileSquare(tileSquare);
    } else {
      player.setSelectedTileSquare(null);
    }
  }

  public boolean getQuit() {
    return quit;
  }

  public float getY() {
    return y;
  }

  public float getX() {
    return x;
  }

  public void setQuit() {
    quit = true;
  }
  
  public boolean getHaveMouseCoords() {
    return haveMouseCoords;
  }
}
