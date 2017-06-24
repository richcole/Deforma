package game.controllers;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.math.Vector;
import game.terrain.Terrain;

public class TerrainPlayerController {

	private Terrain terrain;
	private Player player;

	public TerrainPlayerController(EventBus eventBus, Clock clock, Player player, Terrain terrain) {
		this.terrain = terrain;
		this.player = player;
		clock.onTick((e) -> onTickEvent(e));
	}

	public void onTickEvent(TickEvent event) {
		if (terrain.insideTerrain(player.getPosition())) {
	 		Vector footPos = player.getPosition().minus(Vector.U2.times(0.6));
			byte footTile = terrain.getTerrain(footPos);
			
			if ( footTile == 1 ) {
				// inside rock to float upwards
				player.move(Vector.U2.times(0.05));
			}
			else {
				Vector m = Vector.U2.times(-0.05);
				if ( terrain.getTerrain(footPos.plus(m)) == 0 ) {
					player.tryMove(Vector.U2.times(-0.05), false);
				}
			}
		}
	}
	
	public Vector tryMove(Vector x, Vector dx, boolean playerMove) {
		if (terrain.insideTerrain(player.getPosition())) {

			Vector ndx;
			int[] cs = {0, -1, 1};
			int[][] css = {
				{0,  0, -1},
				{0,  0,  1},

				{-1, 0,  0},
				{ 1, 0,  0},

				{-1, 0,  1},
				{ 1, 0,  1},

				{-1, 0, -1},
				{ 1, 0, -1},

				{0,  -1, -1},
				{0,  -1,  1},

				{-1, -1,  0},
				{ 1, -1,  0},

				{-1, -1,  1},
				{ 1, -1,  1},

				{-1, -1, -1},
				{ 1, -1, -1},
			};

			if (playerMove) {
				ndx = new Vector(dx.x(), 0.0, dx.z(), 1.0);
			}
			else {
				ndx = dx;
			}
			Vector nx = x.plus(ndx);
			Vector terrainLowerLeft = nx.floor();

			int count = 0;
			int maxCount = 12;
			while(count < maxCount) {
				boolean foundConflict = false;
				for(int i=0; i<css.length; ++i) {
					int cx = css[i][0];
					int cy = css[i][1];
					int cz = css[i][2];
					Vector df = new Vector(cx, cy, cz, 1.0);  // direction of face
					Vector neighbourCenter = terrainLowerLeft.plus(new Vector(cx + 0.5, cy + 0.5, cz + 0.5, 1.0));
					Vector neighbourLowerLeft = neighbourCenter.floor();
					byte neighbourBlockType = terrain.getTerrain(neighbourCenter);
					if (neighbourBlockType != 0) {
						Vector dnx = neighbourCenter.minus(nx).normalize().times(0.5);
						Vector midpoint = nx.plus(dnx);
						Vector midpointLowerLeft = midpoint.floor();
						if (midpointLowerLeft.equals(neighbourLowerLeft)) {
							Vector dndx = df.times(df.dot(ndx));
							ndx = ndx.minus(dndx);
							nx = x.plus(ndx);
							terrainLowerLeft = nx.floor();
							count += 1;
							foundConflict = true;
						}
					}
				}

				if(! foundConflict) {
					break;
				}
			}

			if (count == maxCount) {
				return Vector.Z;
			}

			return ndx;
		}

		return dx;
	}
	
}
