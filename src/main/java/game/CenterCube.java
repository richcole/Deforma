package game;

import game.events.EventBus;
import game.events.ViewUpdatedEvent;
import game.geom.CubesGeom;
import game.geom.VertexCloud;


public class CenterCube {
  
  private EventBus eventBus;
  private View view;
  private RenderableModel model;

  public CenterCube(EventBus eventBus, View view, SimpleProgram simpleProgram, Material material) {
    this.eventBus = eventBus;
    this.view = view;
    
    CubesGeom cubesGeom = new CubesGeom(new VertexCloud(material));
    cubesGeom.addCube(Vector.Z, 0.1);
    this.model = new RenderableModel(new CompiledMesh(eventBus, simpleProgram, cubesGeom.getVertexCloud()), Matrix.IDENTITY);
    
    eventBus.onEventType(view, (e) -> onViewUpdate(), ViewUpdatedEvent.class);
  }

  private void onViewUpdate() {
    Vector dx = view.getPosition().plus(view.getForward().times(1.5).plus(view.getLeft().times(1.0))); 
    model.setModelTr(Matrix.translate(dx));
  }

  public Renderable getModel() {
    return model;
  }

}
