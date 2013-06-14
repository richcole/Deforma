package game;

import game.base.Texture;
import game.base.Textures;
import game.math.Vector;
import game.models.BigCube;
import game.models.Light;
import game.models.Creature;
import game.models.Model;
import game.models.Models;
import game.models.SkyBox;
import game.models.Terrain;
import game.models.TerrainTile;
import game.nwn.readers.KeyReader;

import java.io.File;
import java.util.List;

import org.apache.log4j.BasicConfigurator;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Context {

  View view;
  Scene scene;
  InputDevice inputDevice;
  Simulator simulator;
  BigCube bigCube;
  LogPanel  logPanel;
  Main      main;
  Texture stoneTexture;
  Player player;
  SkyBox skyBox;
  Light light;
  int lightNumber;
  Material material;
  Colors colors;
  Creature creature;
  Terrain terrain;
  File root;
  
  Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
  KeyReader keyReader;
  Textures textures;
  SelectionRay selectionRay;
  Models models;

  List<File> roots = Lists.newArrayList(
    new File("/home/local/ANT/richcole/clients/other/nwn-stuff/nwn/"),
    new File("/mnt/nwn/")
  );

  public Context() {
    configureLoghing();
  }
  
  public Scene getScene() {
    if ( scene == null ) {
      scene = new Scene(this);
    }
    return scene;
  }

  public InputDevice getInputDevice() {
    if ( inputDevice == null ) {
      inputDevice = new InputDevice(this);
    }
    return inputDevice;
  }

  public Simulator getSimulator() {
    if ( simulator == null ) {
      simulator = new Simulator(this);
    }
    return simulator;
  }

  View getView() {
    if ( view == null ) {
      view = new View(this);
    }
    return view;
  }

  public Player getPlayer() {
    if ( player == null ) {
      player = new Player(this);
    }
    return player;
  }

  public Main getMain() {
    if ( main == null ) {
      main = new Main(this);
    }
    return main;
  }

  public Texture getStoneTexture() {
    if ( stoneTexture == null ) {
      stoneTexture = getTextures().getFileTexture("res/image.jpg");
    }
    return stoneTexture;
  }

  public BigCube getBigCube() {
    if ( bigCube == null ) {
      bigCube = new BigCube(this);
    }
    return bigCube;
  }

  public Creature newCreature() {
    return new Creature(this, Model.Wererat);
  }

  public TerrainTile newTile() {
    return new TerrainTile(this, new Vector(-20, -20, 0, 1), Model.Tcn01_r10_01);
  }

  public SkyBox getSkyBox() {
    if ( skyBox == null ) {
      skyBox = new SkyBox(this);
    }
    return skyBox;
  }

  public LogPanel getLogPanel() {
    if ( logPanel == null ) {
      logPanel = new LogPanel(this);
    }
    return logPanel;
  }

  public int getNextLightNumber() {
    if ( lightNumber == 0 ) {
      lightNumber = 0;
    } else {
      lightNumber += 1;
    }
    return lightNumber;
  }

  public Material getMaterial() {
    if ( material == null ) {
      material = new Material(this);
    }
    return material;
  }
  
  public Colors getColors() {
    if ( colors == null ) {
      colors = new Colors();
    }
    return colors;
  }

  public Gson getGson() {
    return gson;
  }
  
  public File getNwnRoot() {
    if ( root == null ) {
      for(File cand: roots) {
        if ( cand.exists() ) {
          root = cand;
        }
      }
    }
    if ( root == null ) {
      throw new RuntimeException("Unable to locate nwn root directory");
    }
    return root;
  }
  
  public KeyReader getKeyReader() {
    if ( keyReader == null ) {
      keyReader = new KeyReader(this);
    }
    return keyReader;
  }

  public Textures getTextures() {
    if ( textures == null ) {
      textures = new Textures();
    }
    return textures;
  }
  
  public Terrain getTerrain() {
    if ( terrain == null ) {
      terrain = new Terrain(this, 20, 20, 20f);
    }
    return terrain;
  }

  public void configureLoghing() {
    BasicConfigurator.configure();
  }
  
  public SelectionRay getSelectionRay() {
    if ( selectionRay == null ) {
      selectionRay = new SelectionRay(this);
    }
    return selectionRay;
  }

  public Models getModels() {
    if ( models == null ) {
      models = new Models(this);
    }
    return models;
  }

  public double getScale() {
    return 10;
  }
}
