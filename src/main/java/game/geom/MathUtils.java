package game.geom;

public class MathUtils {

  public static double toDegrees(double angleInRadians) {
    return angleInRadians * 360 / (2*Math.PI);
  }
}
