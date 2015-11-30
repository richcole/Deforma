package game;

import game.events.EventBus;
import game.events.KeyDownEvent;
import game.events.KeyUpEvent;
import game.events.MouseButtonEvent;
import game.events.MouseMoveEvent;

public class InputController {
  
  public InputController(Object object, EventBus eventBus) {
    eventBus.onEventType(object, (KeyDownEvent e) -> onKeyDownEvent(e), KeyDownEvent.class);
    eventBus.onEventType(object, (KeyUpEvent e) -> onKeyUpEvent(e), KeyUpEvent.class);
    eventBus.onEventType(object, (MouseMoveEvent e) -> onMouseMoveEvent(e), MouseMoveEvent.class);
    eventBus.onEventType(object, (MouseButtonEvent e) -> onMouseButtonEvent(e), MouseButtonEvent.class);
  }
  
  void onKeyDownEvent(KeyDownEvent e) {};
  void onKeyUpEvent(KeyUpEvent e) {};
  void onMouseMoveEvent(MouseMoveEvent e) {};
  void onMouseButtonEvent(MouseButtonEvent e) {};

}
