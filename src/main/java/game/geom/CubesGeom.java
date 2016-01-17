package game.geom;

import game.Geom;
import game.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CubesGeom {

  final static Logger log = LoggerFactory.getLogger(CubesGeom.class);

  private VertexCloud vertexCloud;

  public CubesGeom(VertexCloud vertexCloud) {
    this.vertexCloud = vertexCloud;
  }
  
  public void addCube(Vector center, double radius) {
    log.info("Add cube " + center);
    Vector dp = Vector.ONES.times(radius);
    for(int i=0;i<3;++i) {
      addFace(center, radius, dp, i, 1);
      addFace(center, radius, dp, i, -1);
    }    
  }

  public void addFace(Vector center, double radius, Vector dp, int dim, double sign) {
    Vector n  = Vector.UNIT(dim).times(sign);
    Vector dn = n.times(radius);
    Vector dx = (dim == 0 ? Vector.UNIT(1) : Vector.UNIT(0)).times(radius);
    Vector dy = (dim == 0 ? Vector.UNIT(2) : (dim == 1 ? Vector.UNIT(2) : Vector.UNIT(1))).times(radius);
    
    Vector bl = center.plus(dn).minus(dx).minus(dy);
    Vector tr = center.plus(dn).plus(dx).plus(dy);
    Vector br = center.plus(dn).plus(dx).minus(dy);
    Vector tl = center.plus(dn).minus(dx).plus(dy);

    addFace(n, bl, tr, br, Vector.BL, Vector.TR, Vector.BR);
    addFace(n, bl, tl, tr, Vector.BL, Vector.TL, Vector.TR);
  }

  private void addFace(Vector n, Vector v1, Vector v2, Vector v3, Vector t1, Vector t2, Vector t3) {
    vertexCloud.addVertex(v1, n, t1);
    vertexCloud.addVertex(v2, n, t2);
    vertexCloud.addVertex(v3, n, t3);
  }

  public Geom getVertexCloud() {
    return vertexCloud;
  }

}
