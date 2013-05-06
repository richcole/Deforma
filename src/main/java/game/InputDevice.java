package game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

public class InputDevice {
  
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
          grabbed = ! grabbed;
          Mouse.setGrabbed(grabbed);
          haveMouseCoords = false;
        }
        break;
      case Keyboard.KEY_Q:
      case Keyboard.KEY_ESCAPE:
        quit = true;
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
      }
      eventProcessed = true;
    }
    return eventProcessed;
  }

  private boolean processMouse() {
    if ( ! grabbed ) {
      while( Mouse.next() );
      return false;
    }
    boolean eventProcessed = false;
    while( Mouse.next() ) {
      if ( haveMouseCoords ) {
        x += Mouse.getX() - mx;
        y += Mouse.getY() - my;
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
    return eventProcessed;
  }
  
  boolean getQuit() {
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
}
