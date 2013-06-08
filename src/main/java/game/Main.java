package game;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

public class Main {
  
  private static Logger logger = Logger.getLogger(Main.class);
  Context context;

  public Main(Context context) {
    this.context = context;
  }

  public static void main(String[] args) {
    BasicConfigurator.configure();
    openDisplay();
    try {
      new Context().getMain().run();
    }
    catch(RuntimeException e) {
      logger.info("Exception raised in main", e);
    }
    finally {
      closeDisplay();
    }
  }

  private static void closeDisplay() {
    Display.destroy();
  }
  
  private void run() {
    context.getView().init();
    context.getLogPanel();

    context.getSimulator().start();
    try {
      int lastTick = 0;
      context.getBigCube();
      context.getSkyBox();
      context.getScene().register(context.getBigCube());
      context.getScene().register(context.getSkyBox());
      context.getScene().register(context.getRat());
      context.getScene().register(context.getRat());
      context.getSimulator().register(context.getRat());
      context.getSimulator().register(context.getPlayer());
      while (! context.getInputDevice().getQuit()) {
        long before = System.currentTimeMillis();
        if ( context.getInputDevice().process() || context.getSimulator().getCurrentTick() != lastTick ) {
          lastTick = context.getSimulator().getCurrentTick();
          context.getScene().render();
          Display.update();
        }
        long after = System.currentTimeMillis();
        context.getLogPanel().setRenderSpeed(after - before);
      }
    }
    catch(RuntimeException e) {
      logger.info("Exception raised in main", e);
      throw e;
    }
    finally {
      context.getInputDevice().setQuit();
    }
  }


  private static void openDisplay() {
    try {
      DisplayMode mode = new DisplayMode(800, 800);
      Display.setDisplayMode(mode);
      Display.create();
    }
    catch(Exception e) {
      throw new RuntimeException(e);
    }
  }
}
