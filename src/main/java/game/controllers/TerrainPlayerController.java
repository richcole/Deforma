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
	 		Vector footPos = player.getPosition().minus(Vector.U2);
			byte footTile = terrain.getTerrain(footPos);
			
			if ( footTile == 1 ) {
				// inside rock to float upwards
				player.move(Vector.U2.times(0.05));
			}
			else {
				Vector m = Vector.U2.times(-0.05);
				if ( terrain.getTerrain(footPos.plus(m)) == 0 ) {
					player.move(Vector.U2.times(-0.05));
				}
			}
		}
	}
	
	public Vector tryMove(Vector x, Vector dx) {
		if ( false ) {
			return dx;
		}
		if (terrain.insideTerrain(player.getPosition())) {
			Vector dxz = new Vector(dx.x(), 0, dx.z(), 1.0);
			Vector nx = x.plus(dxz).minus(Vector.U2);
			
			// work out the center of the square the character is
			// in
			Vector c = new Vector(Math.round(nx.x()), nx.y(), Math.round(nx.z()), 1.0);
			
			for(int cx=-1;cx<=1;cx+=1) {
				for(int cz=-1;cz<=1;cz+=1) {
					Vector ex = Vector.U1.times(cx);
					Vector ez = Vector.U3.times(cz);
					Vector exz = ex.plus(ez);
					if ( terrain.getTerrain(c.plus(exz)) != 0) {
						Vector cxmn = c.plus(exz.times(0.5)).minus(nx);
						double l = cxmn.lengthSquared();
						if ( l < 0.30 ) {
							return Vector.Z;
						}
					}
				}
			}
			
			return dxz;
		}
		else {
			return dx;
		}
	}
	
}
