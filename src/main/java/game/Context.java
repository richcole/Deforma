package game;

import game.base.Texture;
import game.base.Textures;
import game.models.BigCube;
import game.models.Light;
import game.models.Rat;
import game.models.SkyBox;
import game.nwn.readers.KeyReader;

import java.io.File;

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
  Rat rat;
  
  Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
  File root = new File("/home/local/ANT/richcole/clients/other/nwn-stuff/nwn/");
  KeyReader keyReader;
  Textures textures;

  public Context() {
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

  public Rat getRat() {
    if ( rat == null ) {
      rat = new Rat(this, "c_wererat", 50);
    }
    return rat;
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
    return root;
  }
  
  public KeyReader getKeyReader() {
    if ( keyReader == null ) {
      keyReader = new KeyReader(this, "chitin.key");
    }
    return keyReader;
  }

  public Textures getTextures() {
    if ( textures == null ) {
      textures = new Textures();
    }
    return textures;
  }
}
