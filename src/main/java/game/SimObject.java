package game;

public interface SimObject {
  void tick();
  Vector pos();
  double mass();
}
