package game.models;

import game.basicgeom.Vector;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class ColorFactory {
  public FloatBuffer getColor(Vector diffuse) {
    FloatBuffer buf = BufferUtils.createFloatBuffer(4); 
    buf.put((float)diffuse.get(0));
    buf.put((float)diffuse.get(1));
    buf.put((float)diffuse.get(2));
    buf.put((float)diffuse.get(3));
    buf.flip();
    return buf;
  }

}
