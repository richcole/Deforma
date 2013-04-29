package game;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

import com.google.inject.Guice;
import com.google.inject.Inject;

import static org.lwjgl.opengl.GL11.*;

public class Main {
  
  private static Logger logger = Logger.getLogger(Main.class);

  @Inject private View view;
  @Inject private Scene scene;
  @Inject private InputDevice inputDevice;
  @Inject private Simulator simulator;
  
  public static void main(String[] args) {
    Guice.createInjector(new MainModule()).getInstance(Main.class).run();
  }
  
  private void run() {
    
    try {
      BasicConfigurator.configure();
      openDisplay();
      
      try {
        init(view, scene, inputDevice, simulator);
        view.initGL();
        scene.initGL();
  
        int lastTick = 0;
        while (! inputDevice.getQuit()) {
          if ( inputDevice.process() || simulator.getCurrentTick() != lastTick ) {
            lastTick = simulator.getCurrentTick();
            scene.render();
            Display.update();
          }
        }
      }
      finally {
        Display.destroy();
        inputDevice.setQuit();
      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private void init(HasInit ... inits) {
    for(HasInit init: inits) {
      init.init();
    }
  }

  private void openDisplay() throws LWJGLException {
    // DisplayMode mode = selectLargestMode();
    DisplayMode mode = new DisplayMode(800, 800);
    view.setMode(mode);
    Display.setDisplayMode(mode);
    Display.create();
    logger.info("Mode " + mode.getWidth() + " " + mode.getHeight());
  }

  private DisplayMode selectLargestMode() throws LWJGLException {
    DisplayMode[] modes = Display.getAvailableDisplayModes();
    DisplayMode maxMode = null;
    for (DisplayMode mode : modes) {
      if (maxMode == null || maxMode.getWidth() < mode.getWidth()) {
        maxMode = mode;
      }
    }
    return maxMode;
  }

}
