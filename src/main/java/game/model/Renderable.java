package game.model;

import game.math.Matrix;

public interface Renderable {
  void render(Matrix viewMatrix);
}
