package game.creature;

import game.math.Matrix;
import game.model.CompiledAnimSet;

public class CreatureModel {

	private CompiledAnimSet compiledAnimSet;

	public CreatureModel(CompiledAnimSet compiledAnimSet) {
		this.compiledAnimSet = compiledAnimSet;
	}

	public void render(Matrix viewTr, Matrix modelTr, String animName, int frame, double t) {
		compiledAnimSet.render(viewTr, modelTr, animName, frame, t);
	}

	public int getFrame(String animName, double t) {
		return compiledAnimSet.getFrame(animName, t);
	}


}
