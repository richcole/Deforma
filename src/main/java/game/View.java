package game;

import java.util.List;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class View implements Action {

	final static Logger log = LoggerFactory.getLogger(View.class);

	private final SimpleProgram program;

	Vector position = Vector.U1.times(1.0);
	Matrix viewMatrix = Matrix.IDENTITY;
	Matrix rot = Matrix.IDENTITY;
	
	List<Renderable> renderables = Lists.newArrayList();
	
	View(SimpleProgram program) {
		this.program = program;
		update();
	}

	private void update() {
		viewMatrix = Matrix
			.flip(1)
			.times(Matrix.frustum(-1, 1, 1, -1, 1, 10000))
			.times(rot)
			.times(Matrix.translate(position.minus()));
	}

	void move(Vector dx) {
		position = position.plus(dx);
		update();
	}

	public void run() {
		GL11.glClearColor(0, 0, 0, 1); // black
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glEnable(GL11.GL_DEPTH_TEST);

		program.use();
		program.setViewTr(viewMatrix);

		for(Renderable r: renderables) {
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

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
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
