package game.voxel;

import game.math.Vector;

public class Point {
  byte[] bytes;
  int    bits;
  
  public Point() {
    bytes = new byte[1];
    bytes[0] = 0;
    bits = 1;
  }
  
  public Vector getVector() {
    Vector v = new Vector(0, 0, 0, 1);
    for(int i=0;i<3;++i) {
      double r = 1;
      double t = 0;
      for(int j=0; j<bits; ++j) {
        switch ( getByte(j*3+i) ) {
        case 0:
          t -= r; 
          break;
        case 2:
          t += r;
          break;
        }
        r /= 2;
      }
      v.set(i, t);
    }
    return v;
  }
  
  public void incr() {
    int i=0;
    while(getByte(i) == 2) ++i;
    if ( i >= bits*3 ) {
      bits += 1;
    }
    int v = getByte(i);
    setByte(i, (byte)(v+1));
    for(int j=0;j<i;++j) {
      setByte(j, (byte)0);
    }
  }

  private byte getByte(int index) {
    if ( index < bytes.length ) {
      return bytes[index];
    } else {
      return 0;
    }
    
  }

  private void setByte(int index, byte value) {
    resize(index);
    bytes[index] = value;
  }

  private void resize(int bytesIndex) {
    if ( bytesIndex >= bytes.length ) {
      byte[] newBytes = new byte[bytes.length*2];
      for(int i=0;i<bytes.length;++i) {
        newBytes[i] = bytes[i];
      }
      bytes = newBytes;
    }
  }
}
