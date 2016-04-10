package game;

import game.basicgeom.Matrix;
import game.basicgeom.Vector;
import game.events.Clock;
import game.events.DisplayResizeEvent;
import game.events.EventBus;
import game.events.TickEvent;
import game.events.ViewUpdatedEvent;
import game.gl.GLDisplay;

import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class View implements Consumer<TickEvent> {

  final static Logger log = LoggerFactory.getLogger(View.class);

  private SimpleProgram program;
  private GLDisplay display;

  private ViewUpdatedEvent viewUpdatedEvent;
  private EventBus eventBus;

  private Vector position = Vector.U1.times(1.0);
  private Matrix viewMatrix = Matrix.IDENTITY;
  private Matrix rot = Matrix.IDENTITY;
  private Matrix rotInv = Matrix.IDENTITY;

  private List<Renderable> renderables = Lists.newArrayList();

  public View(GLDisplay display, SimpleProgram program, Clock clock, EventBus eventBus) {
    this.eventBus = eventBus;
    this.program = program;
    this.display = display;
    this.viewUpdatedEvent = new ViewUpdatedEvent(this);
    update();
    eventBus.onEventType(clock, this, TickEvent.class);
    eventBus.onEventType(display, (e) -> update(), DisplayResizeEvent.class);
  }

  private void update() {
    double dydx = display.getHeightToWidthRatio();
    double dx = 1.0;
    double dy = dydx * dx;
    viewMatrix = Matrix
        .frustum(-dx, dx, dy, -dy, 1, 10000)
        .times(rot).times(Matrix.translate(position.minus()));
    eventBus.post(viewUpdatedEvent);
  }

  public void move(Vector dx) {
    position = position.plus(dx);
    update();
  }

  public void accept(TickEvent event) {
    GL11.glClearColor(0, 0, 0, 1); // black
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glEnable(GL11.GL_DEPTH_TEST);
//    GL11.glEnable(GL11.GL_BLEND);
//    GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);     

    program.use();
    program.setViewTr(viewMatrix);

    for (Renderable r : renderables) {
      r.render();
    }
    Display.update();
    Display.sync(60);
  }

  public void setRotation(double sx, double sy) {
    Matrix rotX1 = Matrix.rot2(sx, Vector.U2);
    Matrix rotX2 = Matrix.rot2(-sx, Vector.U2);
    
    Matrix rotY1 = Matrix.rot2(sy, Vector.U1);
    Matrix rotY2 = Matrix.rot2(-sy, Vector.U1);
    
    rot = rotY2.times(rotX1);
    rotInv = rotX2.times(rotY1);
    
    update();
  }

  public void add(Renderable r) {
    renderables.add(r);
  }

  public void add(ModelResource r) {
    renderables.add(new RenderableModel(r, Matrix.IDENTITY));
  }

  public Vector getForward() {
    return Vector.M3.times(rot);
  }

  public Vector getLeft() {
    return Vector.M1.times(rot);
  }

  public Vector getUp() {
    return Vector.U2.times(rot);
  }

  public Vector getPosition() {
    return position;
  }
  
  public Matrix getRotation() {
    return rot;
  }

  public Matrix getRotationInv() {
    return rotInv;
  }

  public void addAll(List<? extends Renderable> models) {
    renderables.addAll(models);
  }
}
