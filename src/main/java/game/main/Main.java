package game.main;

import game.Context;
import game.enums.TileSet;
import game.math.Vector;
import game.nwn.readers.set.SetReader.TileSetDescription;
import game.nwn.readers.set.Tile;
import game.proc.HeightMap;
import game.proc.MidPoint;
import game.proc.Perlin;
import game.proc.Perlin2;
import game.shaders.ProgramRenderer;
import game.voxel.CubeMap;
import game.voxel.DensityFunction;
import game.voxel.DualContouring;
import game.voxel.MarchingCubes;
import game.voxel.ScaleTransform;
import game.voxel.SphereDensity;
import game.voxel.SumDensity;
import game.voxel.Tessellation;

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
      context.getSkyBox().register();
      // context.getGrassBox().register();
      
      double size = 20;
      Vector topRight = Vector.ONES.times(size);
      Vector center = topRight.times(0.5);
      double radius = size * 0.25;
      SphereDensity s1 = new SphereDensity(center, radius);
      SphereDensity s2 = new SphereDensity(center.times(0.5), radius);
      DensityFunction df = new SumDensity(s1, s2);
      // Tessellation ts = new MarchingCubes();
      Tessellation ts = new DualContouring();
      new CubeMap(context, Vector.ZERO, topRight, new ScaleTransform(20), df, ts).register();
      
      // context.getGrassSquare().register();
      // context.getBox().register();
      
      // loadModels();
      // context.newCreature().register();
      // context.getTerrain().register();
      
      context.getPlayer().register();

      // context.getHeightMap().register();

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
