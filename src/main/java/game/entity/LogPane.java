package game.entity;

import game.MutableTexture;
import game.Renderable;
import game.RenderableModel;
import game.SimpleProgram;
import game.Stats;
import game.View;
import game.basicgeom.Matrix;
import game.basicgeom.Vector;
import game.events.EventBus;
import game.events.ViewUpdatedEvent;
import game.geom.SquareGeom;
import game.mesh.CompiledMesh;


public class LogPane {
  
  private EventBus eventBus;
  private View view;
  private RenderableModel model;
  private MutableTexture mutableTexture;
  private Stats stats;
  private int yPosition = 0;
  private int xPosition = 0;
  private int lineSpacing = 12 + 6;
  private int xMargin = 2;
  private int yMargin = 2;

  public LogPane(EventBus eventBus, View view, SimpleProgram simpleProgram, Stats stats) {
    this.eventBus = eventBus;
    this.view = view;
    this.stats = stats;
    
    mutableTexture = new MutableTexture(eventBus, 200, 200);
    SquareGeom geom = new SquareGeom(Vector.Z, Vector.U1.times(0.3), Vector.U2.times(0.3), mutableTexture);
    this.model = new RenderableModel(new CompiledMesh(eventBus, simpleProgram, geom), Matrix.IDENTITY);
    
    mutableTexture.update();
    
    eventBus.onEventType(view, (e) -> onViewUpdate(), ViewUpdatedEvent.class);
  }

  private void onViewUpdate() {
    Vector dx = view.getPosition().plus(view.getForward().times(1.001).plus(view.getLeft().times(-0.69)).plus(view.getUp().times(-0.69)));
    view.getRotationInv();
    model.setModelTr(Matrix.translate(dx).times(view.getRotationInv()));
    
    reset();
    
    formatLine("gravity.p=%s", stats.gravityP);
    formatLine("view.p=%s", stats.viewP);
    formatLine("gravity.insideField=%s", stats.insideField);
    mutableTexture.update();
  }

  private void reset() {
    mutableTexture.clear();
    yPosition = lineSpacing + yMargin;
    xPosition = xMargin;
  }

  private void formatLine(String string, Object ... args) {
    mutableTexture.drawText(String.format(string, args), xPosition, yPosition);
    yPosition += lineSpacing;
  }

  public Renderable getModel() {
    return model;
  }

}
