package game.proc;

import game.Renderable;
import game.math.Vector;

import java.nio.DoubleBuffer;
import java.util.List;
import java.util.Map;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class VertexCloud implements Renderable {
  
  int vId, pId, nId, tId, oId, uId, rId, pIndex = 0, nIndex = 1, tIndex = 2, oIndex = 3, dxIndex = 4, dyIndex = 5;
  
  List<Vector> positions;
  List<Vector> normals;
  List<Vector> textures;
  List<Vector> origins;
  List<Vector> dxs;
  List<Vector> dys;
  
  public VertexCloud() {
    positions = Lists.newArrayList();
    normals = Lists.newArrayList();
    textures = Lists.newArrayList();
    origins = Lists.newArrayList();
    dxs = Lists.newArrayList();
    dys = Lists.newArrayList();
  }
  
  public void addVertex(Vector p, Vector n, Vector t) {
    Vector z = Vector.ZERO;
    addVertex(p, n, t, z, z, z);
  }
  
  public void addVertex(Vector p, Vector n, Vector t, Vector o, Vector dx, Vector dy) {
    Preconditions.checkNotNull(p);
    Preconditions.checkNotNull(n);
    Preconditions.checkNotNull(t);
    
    positions.add(p);
    normals.add(n);
    textures.add(t);
    origins.add(o);
    dxs.add(dx);
    dys.add(dy);
  }
  
  public void freeze() {
    vId = GL30.glGenVertexArrays();
    GL30.glBindVertexArray(vId);
    
    int len = positions.size();
    pId = setVertexData(pIndex, positions, len);
    nId = setVertexData(nIndex, normals, len);
    tId = setVertexData(tIndex, textures, len);
    oId = setVertexData(oIndex, origins, len);
    uId = setVertexData(dxIndex, dxs, len);
    rId = setVertexData(dyIndex, dys, len);
    
    GL30.glBindVertexArray(0);
  }

  private int setVertexData(int index, List<Vector> vs, int expectedLen) {
    if ( vs.size() != expectedLen ) {
      throw new RuntimeException("expected different length");
    }
    int id = GL15.glGenBuffers();
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, id);
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, createBuf(vs), GL15.GL_STATIC_DRAW);
    GL20.glVertexAttribPointer(index, 3, GL11.GL_DOUBLE, false, 0, 0);
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    return id;
  }

  private DoubleBuffer createBuf(List<Vector> vs) {
    DoubleBuffer buf = BufferUtils.createDoubleBuffer(vs.size()*3);
    for(Vector v: vs) {
      buf.put(v.x());
      buf.put(v.y());
      buf.put(v.z());
    }
    buf.flip();
    return buf;
  }
  
  public void render() {
    GL30.glBindVertexArray(vId);
    GL20.glEnableVertexAttribArray(pIndex);
    GL20.glEnableVertexAttribArray(nIndex);
    GL20.glEnableVertexAttribArray(tIndex);
    GL20.glEnableVertexAttribArray(oIndex);
    GL20.glEnableVertexAttribArray(dxIndex);
    GL20.glEnableVertexAttribArray(dyIndex);
    
    GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, positions.size());

    GL20.glDisableVertexAttribArray(pIndex);
    GL20.glDisableVertexAttribArray(nIndex);
    GL20.glDisableVertexAttribArray(tIndex);
    GL20.glDisableVertexAttribArray(oIndex);
    GL20.glDisableVertexAttribArray(dxIndex);
    GL20.glDisableVertexAttribArray(dyIndex);
    GL30.glBindVertexArray(0);
  }

  public void computeNormals() {
    for(int i=0;i<positions.size(); i+=3) {
      Vector p1 = positions.get(i);
      Vector p2 = positions.get(i+1);
      Vector p3 = positions.get(i+2);
      Vector n = p2.minus(p1).cross(p3.minus(p1)).normalize();
      normals.set(i, n);
      normals.set(i+1, n);
      normals.set(i+2, n);
    }
    
  }
}
