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
		if (terrain.insideTerrain(player.getPosition())) {
			Vector dxz = new Vector(dx.x(), 0, dx.z(), 1.0);
			Vector nx = x.plus(dxz).minus(Vector.U2);
			Vector p1 = nx.plus(Vector.U1.times(0.5));
			Vector p2 = nx.plus(Vector.U3.times(0.5));
			Vector p3 = nx.minus(Vector.U1.times(0.5));
			Vector p4 = nx.minus(Vector.U3.times(0.5));
			
			if ( terrain.getTerrain(p1) != 0 ) {
				return Vector.Z;
			}
			if ( terrain.getTerrain(p2) != 0 ) {
				return Vector.Z;
			}
			if ( terrain.getTerrain(p3) != 0 ) {
				return Vector.Z;
			}
			if ( terrain.getTerrain(p4) != 0 ) {
				return Vector.Z;
			}
			
			return dxz;
		}
		else {
			return dx;
		}
	}
	
}
