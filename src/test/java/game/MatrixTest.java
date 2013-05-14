package game;


import game.math.Matrix;
import game.math.Vector;

import org.junit.Assert;
import org.junit.Test;

public class MatrixTest {

  @Test
  public void testRot0() {
    Matrix b = Matrix.BASIS;
    Matrix c = b.times(Matrix.rot(0f, Vector.U1));
    Assert.assertTrue(b.withinDelta(c, 1e-6));
  }

  @Test
  public void testRot90() {
    Matrix b = Matrix.BASIS;
    Matrix r = Matrix.rot(Math.PI/2, Vector.U1);
    Matrix c = b.times(r);
    Matrix e = Matrix.rows(Vector.U1, Vector.U3.minus(), Vector.U2, Vector.ZERO);
    System.out.println("r=" + c);
    System.out.println("c=" + c);
    Assert.assertTrue(e.withinDelta(c, 1e-6));
  }
}
