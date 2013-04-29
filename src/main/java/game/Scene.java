package game;

import com.google.inject.Inject;

public class Scene implements HasInit, HasRender  {
  
  @Inject Rectangle rectangle;
  @Inject LookAt    lookAt;
  @Inject LogPanel  logPanel;
  @Inject View      view;
  
  public void init() {
    lookAt.init();
    rectangle.init();
    logPanel.init();
  }

  public void render() {
    view.perspectiveView();
    view.clear();
    lookAt.render();
    rectangle.render();

    view.orthoView();
    logPanel.render();
  }
  
  public void tick() {
    lookAt.tick();
  }
  
  public void initGL() {
    rectangle.initGL();
  }
  
}
