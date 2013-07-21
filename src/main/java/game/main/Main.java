package game.main;

import game.Context;
import game.enums.TileSet;
import game.nwn.readers.set.SetReader.TileSetDescription;
import game.nwn.readers.set.Tile;
import game.proc.HeightMap;
import game.proc.MidPoint;
import game.proc.Perlin;
import game.proc.Perlin2;
import game.shaders.Box;

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
    Context context = new Context();
    openDisplay();
    try {
      context.getMain().run();
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
    
    try {
      // context.getScene().register(new Box(context));
      context.getScene().register(context.getSkyBox());

      // loadModels();
      // context.newCreature().register();
      // context.getTerrain().register();
      
      context.getPlayer().register();
      
      HeightMap heightMap = context.getHeightMap();
      new Perlin2(12).generate(heightMap, heightMap.getWidth());
      heightMap.calculateNormals();
      heightMap.register();

      context.getSimulator().start();
      context.getSimulator().waitForStart();
      while (! context.getInputDevice().getQuit()) {
        long before = System.currentTimeMillis();
        context.getInputDevice().process();
        context.getScene().render();
        Display.update();
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

  private void loadModels() {
    // ensure textures for all models are loaded
    TileSetDescription tileSetDescription = context.getTileSetDescriptions().getTileSetDescription(TileSet.Tin01);
    for(Tile tile: tileSetDescription.getTiles()) {
      for(String textureName: context.getModels().getAnimMesh(tile.getModel()).getTextures()) {
        context.getTilingTextures().getFileTexture(textureName + ".tga");
      }
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
