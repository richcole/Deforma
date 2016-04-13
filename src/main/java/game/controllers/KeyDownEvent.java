package game.controllers;

import game.events.Event;

public class KeyDownEvent implements Event {

  public int key;

  public KeyDownEvent(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }

}
