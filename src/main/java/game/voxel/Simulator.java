package game.voxel;

import java.util.List;

import com.google.common.collect.Lists;

public class Simulator implements Action {
	
	long lastTick;
	long tickTime = 10;
	
	List<Simulant> simulants = Lists.newArrayList();
	
	Simulator() {
		lastTick = System.currentTimeMillis();
	}
	
	public void run() {
		long tick = System.currentTimeMillis();
		if (tick - lastTick > tickTime) {
			lastTick = tick;
			for(Simulant sim: simulants) {
				sim.tick(tickTime);
			}
		}
	}

	public void init() {
		// TODO Auto-generated method stub
		
	}

	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	public void add(Simulant simulant) {
		simulants.add(simulant);
	}
	
}
