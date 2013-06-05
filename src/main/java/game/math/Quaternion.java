package game.math;

public class Quaternion extends Vector {

  public Quaternion() {
    super();
  }

  public Quaternion(double x1, double x2, double x3, double x4) {
    super(x1, x2, x3, x4);
  }

  public Quaternion(Vector o) {
    super(o);
  }
  
  public Matrix toMatrix() {
    double qx = v[0];
    double qy = v[1];
    double qz = v[2];
    double qw = v[3];
    
    return new Matrix(
      1.0f - 2.0f*qy*qy - 2.0f*qz*qz, 2.0f*qx*qy - 2.0f*qz*qw, 2.0f*qx*qz + 2.0f*qy*qw, 0.0f,
      2.0f*qx*qy + 2.0f*qz*qw, 1.0f - 2.0f*qx*qx - 2.0f*qz*qz, 2.0f*qy*qz - 2.0f*qx*qw, 0.0f,
      2.0f*qx*qz - 2.0f*qy*qw, 2.0f*qy*qz + 2.0f*qx*qw, 1.0f - 2.0f*qx*qx - 2.0f*qy*qy, 0.0f,
      0.0f, 0.0f, 0.0f, 1.0f
    );
  }

  public Quaternion times(double s) {
    return new Quaternion(v[0]*s, v[1]*s, v[2]*s, v[3]*s);
  }

  public Quaternion plus(Quaternion o) {
    return new Quaternion(v[0] + o.v[0], v[1] + o.v[1], v[2] + o.v[2], v[3] + o.v[3]);
  }
}
