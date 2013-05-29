package game;
import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;


public class Colors {

  private FloatBuffer white = createColor(1f, 1f, 1f, 1f);
  private FloatBuffer gray2 = createColor(0.2f, 0.2f, 0.2f, 0.2f);
  private FloatBuffer gray9 = createColor(0.9f, 0.9f, 0.9f, 0.9f);
  
  Colors() {
  }

  FloatBuffer createColor(float r, float g, float b, float a) {
    FloatBuffer buf = BufferUtils.createFloatBuffer(4);
    buf.put(r).put(g).put(b).put(a).flip();
    return buf;
  }

  public FloatBuffer getWhite() {
    return white;
  }

  public FloatBuffer getGray9() {
    return gray9;
  }
  
  public FloatBuffer getGray2() {
    return gray9;
  }
}
