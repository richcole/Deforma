package game.creature;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.math.Matrix;
import game.model.Renderable;

public class Creature implements Renderable {

	private CreatureModel creatureModel;
	private Matrix tr;
	private String animName;
	int frame = 0;
	private double t = 0;

	public Creature(Clock clock, EventBus eventBus, CreatureModel creatureModel, Matrix tr, String animName) {
		this.creatureModel = creatureModel;
		this.tr = tr;
		this.animName = animName;
		
		eventBus.onEvent(clock, TickEvent.class, (e) -> onTick(e));
	}

	private void onTick(TickEvent e) {
		t = t + e.dt;
		frame = creatureModel.getFrame(animName, t);
	}

	@Override
	public void render(Matrix viewTr) {
		creatureModel.render(viewTr, tr, animName, frame, t);
	}
}
