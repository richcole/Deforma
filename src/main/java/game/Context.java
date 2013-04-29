package game;

public class Context {

  StoneTexture stoneText;
  View view;
  Scene scene;
  InputDevice inputDevice;
  Simulator simulator;
  BorgShipRectangle borgShipRectangle;
  LookAt    lookAt;
  LogPanel  logPanel;
  Main      main;
  StoneTexture stoneTexture;
  
  Context() {
  }
  
  public StoneTexture getStoneText() {
    if ( stoneText == null ) {
      stoneText = new StoneTexture(this);
    }
    return stoneText;    
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

  public LookAt getLookAt() {
    if ( lookAt == null ) {
      lookAt = new LookAt(this);
    }
    return lookAt;
  }

  public Main getMain() {
    if ( main == null ) {
      main = new Main(this);
    }
    return main;
  }

  public StoneTexture getStoneTexture() {
    if ( stoneTexture == null ) {
      stoneTexture = new StoneTexture(this);
    }
    return stoneTexture;
  }

  public BorgShipRectangle getBorgShipRectangle() {
    if ( borgShipRectangle == null ) {
      borgShipRectangle = new BorgShipRectangle(this);
    }
    return borgShipRectangle;
  }

  public LogPanel getLogPanel() {
    if ( logPanel == null ) {
      logPanel = new LogPanel(this);
    }
    return logPanel;
  }
  
}
