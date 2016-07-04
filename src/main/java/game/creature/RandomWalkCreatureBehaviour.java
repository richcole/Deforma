package game.creature;

import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.math.Matrix;
import game.math.Vector;

public class RandomWalkCreatureBehaviour implements CreatureBehaviour {
	
	private static Logger log = LoggerFactory.getLogger(Logger.class); 
	
	private Creature creature;
	private Random random;
	private Vector dest;

	public RandomWalkCreatureBehaviour(Creature creature) {
		this.creature = creature;
		this.random = new Random();
		this.dest = new Vector(random.nextDouble(), 0.0, random.nextDouble(), 1.0).times(100.0).minus(new Vector(50, 0.0, 50, 1.0)); 
	}
	
	@Override
	public void tick() {
		double theta = creature.getTheta();
		Vector pos = creature.getPos();
		Vector dx  = dest.minus(pos);
		Vector ndx = new Vector(dx.x(), 0.0, dx.z(), 1.0).normalize();
		double thetaDest = Math.atan2(ndx.x(), ndx.z());
		if ( thetaDest < 0 ) {
			thetaDest = thetaDest + 2 * Math.PI ;
		}
		if ( theta > thetaDest ) {
			theta = Math.max(thetaDest, theta - 0.1);
		}
		else {
			theta = Math.min(thetaDest, theta + 0.1);
		}
		if ( theta < 0 ) {
			theta = thetaDest + 2 * Math.PI;
		}
		Vector f = Matrix.rot(theta, Vector.U2).times(Vector.U3).times(0.05);
		pos = pos.plus(f);
		// log.info("theta " + theta + " thetaDest " + thetaDest);
		creature.setPlacement(pos, theta);
	}
}
