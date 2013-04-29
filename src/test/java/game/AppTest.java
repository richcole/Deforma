package game;

import org.junit.Assert;
import org.junit.Test;


public class AppTest {
  
  float delta = 1e-7f;
  

  @Test
  public void testLookAt() {
    LookAt lookAt = new LookAt();
    lookAt.init();
    lookAt.recalc();
    Assert.assertEquals(0f, lookAt.f.x, delta); 
    Assert.assertEquals(0f, lookAt.f.y, delta); 
    Assert.assertEquals(1f, lookAt.f.z, delta);
    
    Assert.assertEquals(1f,   lookAt.s.x, delta); 
    Assert.assertEquals(0f,   lookAt.s.y, delta); 
    Assert.assertEquals(0f,   lookAt.s.z, delta);
    
    Assert.assertEquals(0f,   lookAt.nf.x, delta); 
    Assert.assertEquals(0f,   lookAt.nf.y, delta); 
    Assert.assertEquals(1f,   lookAt.nf.z, delta);
    
    Assert.assertEquals(0f,   lookAt.nu.x, delta); 
    Assert.assertEquals(1f,   lookAt.nu.y, delta); 
    Assert.assertEquals(0f,   lookAt.nu.z, delta);

    Assert.assertEquals(1f,   lookAt.ns.x, delta); 
    Assert.assertEquals(0f,   lookAt.ns.y, delta); 
    Assert.assertEquals(0f,   lookAt.ns.z, delta);
    
    Assert.assertEquals(0f,   lookAt.nc.x, delta); 
    Assert.assertEquals(0f,   lookAt.nc.y, delta); 
    Assert.assertEquals(-19f,   lookAt.nc.z, delta);
  }

  @Test
  public void testLookAt1() {
    LookAt lookAt = new LookAt();
    lookAt.init();
    lookAt.rotate((float)Math.PI / 2, delta);
    lookAt.recalc();
    
    Assert.assertEquals(0f,   lookAt.nu.x, delta); 
    Assert.assertEquals(1f,   lookAt.nu.y, delta); 
    Assert.assertEquals(0f,   lookAt.nu.z, delta);

    Assert.assertEquals(0f,   lookAt.ns.x, delta); 
    Assert.assertEquals(0f,   lookAt.ns.y, delta); 
    Assert.assertEquals(1f,   lookAt.ns.z, delta);
    
    Assert.assertEquals(-1f,   lookAt.nc.x, delta); 
    Assert.assertEquals(0f,   lookAt.nc.y, delta); 
    Assert.assertEquals(-20f, lookAt.nc.z, delta);
  }

  @Test
  public void testLookAt2() {
    LookAt lookAt = new LookAt();
    lookAt.init();
    lookAt.rotate((float)Math.PI, delta);
    lookAt.recalc();
    
    Assert.assertEquals(0f,   lookAt.nu.x, delta); 
    Assert.assertEquals(1f,   lookAt.nu.y, delta); 
    Assert.assertEquals(0f,   lookAt.nu.z, delta);

    Assert.assertEquals(-1f,  lookAt.ns.x, delta); 
    Assert.assertEquals(0f,   lookAt.ns.y, delta); 
    Assert.assertEquals(0f,   lookAt.ns.z, delta);
    
    Assert.assertEquals(0f,   lookAt.nc.x, delta); 
    Assert.assertEquals(0f,   lookAt.nc.y, delta); 
    Assert.assertEquals(-21f, lookAt.nc.z, delta);
  }
}

