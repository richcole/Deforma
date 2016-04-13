package game.controllers;

import game.events.Event;

public class MouseButtonEvent implements Event {
  public int button;
  public boolean isDown;
  
  public MouseButtonEvent(int button, boolean isDown) {
    this.button = button;
    this.isDown = isDown;
  }

}
