package game.controllers;

import game.events.Event;

public class KeyUpEvent implements Event {

  public int key;

  public KeyUpEvent(int key) {
    this.key = key;
  }

  public int getKey() {
    return key;
  }

}
