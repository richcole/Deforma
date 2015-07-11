package game;

import art.SimpleProgram;
import game.math.Matrix;
import game.math.Quaternion;
import game.math.Vector;
import org.lwjgl.opengl.GL20;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class View implements Renderable {

    final static Logger log = LoggerFactory.getLogger(View.class);

    private final SimpleProgram program;

    Vector position = Vector.U1.times(1.0);
    Quaternion rotation = Quaternion.ZERO;
    Matrix viewMatrix = Matrix.IDENTITY;

    View(SimpleProgram program) {
        this.program = program;
        update();
    }

	private void update() {
		viewMatrix = Matrix.flip(1).times(Matrix.frustum(-1, 1, 1, -1, 1, 1000)).times(Matrix.translate(position.minus()));
        log.info("matrix " + viewMatrix);
        log.info("tr " + viewMatrix.times(Vector.U3.times(3).minus()));
	}
    
    void move(Vector dx) {
    	position = position.plus(dx);
    	update();
    }

    public void render() {
        program.use();
        GL20.glUniformMatrix4(program.getTr(), true, viewMatrix.toBuf());
    }
}
