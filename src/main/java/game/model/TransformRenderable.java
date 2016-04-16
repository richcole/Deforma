package game.model;

import game.math.Matrix;

public interface TransformRenderable {
  void render(Matrix viewTr, Matrix modelTr);
}
