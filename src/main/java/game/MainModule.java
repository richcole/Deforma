package game;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;

public class MainModule extends AbstractModule {

  @Override
  public void configure() {
    bind(View.class).in(Singleton.class);
    bind(LookAt.class).in(Singleton.class);
    bind(Scene.class).in(Singleton.class);
    bind(InputDevice.class).in(Singleton.class);
    bind(Simulator.class).in(Singleton.class);
    bind(Rectangle.class).in(Singleton.class);  
    bind(LogPanel.class).in(Singleton.class);
    bind(StoneTexture.class).in(Singleton.class);
  }
  
}
