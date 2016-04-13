package game.gl;

import game.events.Event;

public class DisplayResizeEvent implements Event {

  public int width;
  public int height;

  public DisplayResizeEvent(int width, int height) {
    this.width = width;
    this.height = height;
  }

}
