package game;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import com.google.inject.Inject;

public class InputDevice implements HasInit  {
  
  @Inject View view;
  @Inject LookAt lookAt;
  
  float mx = 0, my = 0;
  float x = 0, y = 0;
  boolean haveMouseCoords = false;
  boolean quit = false;
  boolean grabbed = false;

  public void init() {
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
        lookAt.moveForward(pressed);
        break;
      case Keyboard.KEY_S:
        lookAt.moveBackward(pressed);
        break;
      case Keyboard.KEY_A:
        lookAt.moveLeft(pressed);
        break;
      case Keyboard.KEY_D:
        lookAt.moveRight(pressed);
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
      float minX = view.getWidth() / 5;
      float minY = view.getHeight() / 5;
      float maxX = view.getWidth() - minX;
      float maxY = view.getHeight() - minY;
      if ( mx <= minX || mx >= maxX || my <= minY || my >= maxY ) {
        haveMouseCoords = false;
        Mouse.setCursorPosition((int)view.getWidth()/2, (int)view.getHeight()/2);
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
