package game;

import org.apache.log4j.Logger;

import com.google.inject.Inject;

public class Simulator implements Runnable, HasInit {
  
  private static Logger logger = Logger.getLogger(Logger.class);
  
  @Inject Scene scene;
  @Inject InputDevice inputDevice;
  
  Thread thread;
  int tickNumber = 0;

  @Override
  public void run() {
    long lastSymCompleted = System.currentTimeMillis();
    long symInterval = 50;
    while( ! inputDevice.getQuit() ) {
      tickNumber += 1;
      scene.tick();
      long afterTime = System.currentTimeMillis();
      long sleepTime = lastSymCompleted + symInterval - afterTime; 
      lastSymCompleted = afterTime;
      if ( sleepTime > 0 ) {
        try { Thread.sleep(sleepTime); } catch(Exception e) { throw new RuntimeException(e); };
      }
    }    
  }
  
  public void init() {
    thread = new Thread(this);
    thread.setDaemon(true);
    thread.start();
    
  }

  public int getCurrentTick() {
    return tickNumber;
  }
  
  

}
