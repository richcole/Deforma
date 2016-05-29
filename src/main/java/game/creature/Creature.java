package game.creature;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.math.Matrix;
import game.math.Vector;
import game.model.Renderable;

public class Creature implements Renderable {

	private CreatureModel creatureModel;
	private CreatureBehaviour creatureBehaviour;
	private Matrix baseModelTr;
	private Matrix tr;
	private String animName;
	int frame = 0;
	private double t = 0;
	private Vector pos;
	private double theta;

	public Creature(Clock clock, EventBus eventBus, CreatureModel creatureModel, Vector pos, double theta, String animName) {
		this.creatureModel = creatureModel;
		this.animName = animName;
		this.baseModelTr = Matrix.rot(Math.PI * 0.5, Vector.U2).times(Matrix.rot(Math.PI * -0.5, Vector.U1).times(Matrix.rot(Math.PI * 0.5, Vector.U3)));
		
		setPlacement(pos, theta);
		eventBus.onEvent(clock, TickEvent.class, (e) -> onTick(e));
	}

	private void onTick(TickEvent e) {
		t = t + e.dt;
		frame = creatureModel.getFrame(animName, t);
		
		if ( creatureBehaviour != null ) {
			creatureBehaviour.tick();
		}
	}
	
	public void setPlacement(Vector pos, double theta) {
		this.pos = pos;
		this.theta = theta;
		Matrix mr = Matrix.rot(theta, Vector.U2);
		Matrix mt = Matrix.translate(pos);
		this.tr = mt.times(mr).times(baseModelTr);
	}

	@Override
	public void render(Matrix viewTr) {
		creatureModel.render(viewTr, tr, animName, frame, t);
	}

	public Vector getPos() {
		return pos;
	}

	public double getTheta() {
		return theta;
	}
	
	public void setCreatureBehaviour(CreatureBehaviour creatureBehaviour) {
		this.creatureBehaviour = creatureBehaviour;
	}
}
