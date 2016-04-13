package game.model;

import game.math.Vector;

import com.google.common.collect.Lists;

public class MeshFactory {

  public Mesh newSquareMesh(Vector center, Vector left, Vector up, String imageName) {
    
    Mesh mesh = new Mesh(4*3, 2);
    
    Vector  n = left.cross(up).normalize();
    Vector bl = center.plus(left).minus(up);
    Vector tl = center.plus(left).plus(up);
    Vector br = center.minus(left).minus(up);
    Vector tr = center.minus(left).plus(up);

    setVertex(mesh, 0, bl, n, 0, 0);
    setVertex(mesh, 1, tl, n, 0, 1);
    setVertex(mesh, 2, tr, n, 1, 1);
    setVertex(mesh, 3, br, n, 1, 0);
    
    mesh.e[0] = 0;
    mesh.e[1] = 1;
    mesh.e[2] = 2;
    
    mesh.e[3] = 0;
    mesh.e[4] = 2;
    mesh.e[5] = 3;
    
    for(int i=0; i<mesh.nv; ++i) {
      mesh.b[i] = 0;
      mesh.i[i] = 0;
    }
    
    mesh.imageList = Lists.newArrayList(imageName);
    
    return mesh;
  }

  private void setVertex(Mesh mesh, int index, Vector p, Vector n, double tx, double ty) {
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
