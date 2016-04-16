package game.model;

import game.math.Matrix;
import game.math.Utils;
import game.math.Vector;

import com.google.common.collect.Lists;

public class MeshFactory {

  public AnimMesh newSquareAnimMesh(Vector center, Vector left, Vector up, String imageName) {
    
    AnimMesh mesh = new AnimMesh(4*3, 2);
    
    Vector  n = left.cross(up).normalize();
    Vector bl = center.plus(left).minus(up);
    Vector tl = center.plus(left).plus(up);
    Vector br = center.minus(left).minus(up);
    Vector tr = center.minus(left).plus(up);

    setVertex(mesh, 0, bl, n, 0, 0);
    setVertex(mesh, 1, tl, n, 0, 1);
    setVertex(mesh, 2, tr, n, 1, 1);
    setVertex(mesh, 3, br, n, 1, 0);
    
    mesh.e = new int[6];
    mesh.e[0] = 0;
    mesh.e[1] = 1;
    mesh.e[2] = 2;
    
    mesh.e[3] = 0;
    mesh.e[4] = 2;
    mesh.e[5] = 3;
    
    mesh.b = Utils.toDoubleArray(0.0, 0.0, 0.0, 0.0);
    mesh.numBones = 1;
    mesh.bTr = Utils.toDoubleArray16(Matrix.IDENTITY);
    
    mesh.i = new int[mesh.nv];
    for(int i=0; i<mesh.nv; ++i) {
      mesh.i[i] = 0;
    }
    
    mesh.imageList = Lists.newArrayList(imageName);
    
    return mesh;
  }

  private void setVertex(AnimMesh mesh, int index, Vector p, Vector n, double tx, double ty) {
    mesh.p[index*3+0] = p.x();
    mesh.p[index*3+1] = p.y();
    mesh.p[index*3+2] = p.z();
    
    mesh.n[index*3+0] = n.x();
    mesh.n[index*3+1] = n.y();
    mesh.n[index*3+2] = n.z();
    
    mesh.t[index*2+0] = tx;
    mesh.t[index*2+1] = ty;
  }
  
}
