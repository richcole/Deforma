package game;

import game.math.Vector;

public interface SimObject {
  void tick();
  Vector pos();
  double mass();
}
