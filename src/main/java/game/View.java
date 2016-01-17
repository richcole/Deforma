package game;

import game.events.Clock;
import game.events.DisplayResizeEvent;
import game.events.EventBus;
import game.events.TickEvent;

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

  private Vector position = Vector.U1.times(1.0);
  private Matrix viewMatrix = Matrix.IDENTITY;
  private Matrix rot = Matrix.IDENTITY;

  private List<Renderable> renderables = Lists.newArrayList();

  View(GLDisplay display, SimpleProgram program, Clock clock, EventBus eventBus) {
    this.program = program;
    this.display = display;
    update();
    eventBus.onEventType(clock, this, TickEvent.class);
    eventBus.onEventType(display, (e) -> update(), DisplayResizeEvent.class);
  }

  private void update() {
    double dydx = display.getHeightToWidthRatio();
    double dx = 1.0;
    double dy = dydx * dx;
    viewMatrix = Matrix.flip(1).times(Matrix.frustum(-dx, dx, dy, -dy, 1, 10000))
        .times(rot).times(Matrix.translate(position.minus()));
  }

  public void move(Vector dx) {
    position = position.plus(dx);
    update();
  }

  public void accept(TickEvent event) {
    GL11.glClearColor(0, 0, 0, 1); // black
    GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    GL11.glEnable(GL11.GL_DEPTH_TEST);

    program.use();
    program.setViewTr(viewMatrix);

    for (Renderable r : renderables) {
      r.render();
    }
    Display.update();
    Display.sync(60);
  }

  public void setRotation(Matrix rot) {
    this.rot = rot;
    update();
  }

  public void add(Renderable r) {
    renderables.add(r);
  }

  public void add(ModelResource r) {
    renderables.add(new RenderableModel(r, Matrix.IDENTITY));
  }

  public Vector getForward() {
    return rot.times(Vector.U3);
  }

  public Vector getPosition() {
    return position;
  }

  public void addAll(List<? extends Renderable> models) {
    renderables.addAll(models);
  }
}
