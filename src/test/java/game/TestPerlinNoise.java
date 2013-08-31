package game;

import game.math.Vector;
import game.voxel.PerlinNoise;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestPerlinNoise {
  
  private static Logger logger = Logger.getLogger(TestPerlinNoise.class);
  
  @BeforeClass
  public static void configureLogging() {
    BasicConfigurator.configure();
  }
  
  @Test
  public void testPerlin() {
    PerlinNoise noise = new PerlinNoise(3);
    for(int i=0;i<16;++i) {
      double v = i*0.1;
      logger.info(String.format("v=%2.2f n=%s", v, noise.turbulence(new Vector(v, v, v, 1.0))));
    }
  }

}
