package game.creature;

import java.util.Random;

import game.math.Matrix;
import game.math.Vector;

public class RandomWalkCreatureBehaviour implements CreatureBehaviour {
	
	private Creature creature;
	private Random random;

	public RandomWalkCreatureBehaviour(Creature creature) {
		this.creature = creature;
		this.random = new Random();
	}
	
	@Override
	public void tick() {
		double theta = creature.getTheta();
		Vector pos = creature.getPos(); 
		Vector f = Matrix.rot(theta, Vector.U2).times(Vector.U3).times(0.05);
		pos = pos.plus(f);
		theta = theta + (random.nextDouble() - 0.5) / 20.0;
		creature.setPlacement(pos, theta);
	}
}
