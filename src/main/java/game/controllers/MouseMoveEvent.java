package game.controllers;

import game.events.Event;

public class MouseMoveEvent implements Event {

  public int dx;
  public int dy;

  public MouseMoveEvent(int dx, int dy) {
    this.dx = dx;
    this.dy = dy;
  }

}
