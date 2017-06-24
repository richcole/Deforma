package physics;

import com.bulletphysics.collision.broadphase.BroadphaseInterface;
import com.bulletphysics.collision.broadphase.DbvtBroadphase;
import com.bulletphysics.collision.dispatch.CollisionDispatcher;
import com.bulletphysics.collision.dispatch.DefaultCollisionConfiguration;
import com.bulletphysics.collision.shapes.BoxShape;
import com.bulletphysics.collision.shapes.SphereShape;
import com.bulletphysics.collision.shapes.StaticPlaneShape;
import com.bulletphysics.dynamics.DiscreteDynamicsWorld;
import com.bulletphysics.dynamics.RigidBody;
import com.bulletphysics.dynamics.RigidBodyConstructionInfo;
import com.bulletphysics.dynamics.constraintsolver.SequentialImpulseConstraintSolver;
import com.bulletphysics.linearmath.DefaultMotionState;
import com.bulletphysics.linearmath.Transform;
import game.controllers.Player;
import game.math.Matrix;
import game.math.Vector;
import game.terrain.Terrain;
import game.terrain.TerrainWithTr;

import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;

public class ChunkPhysics {

	private final TerrainWithTr terrainWithTr;
	private final Player player;

	private final BroadphaseInterface broadphase;
	private final DefaultCollisionConfiguration collisionConfiguration;
	private final CollisionDispatcher dispatcher;
	private final SequentialImpulseConstraintSolver solver;
	private final DiscreteDynamicsWorld dynamicsWorld;

	private final SphereShape playerShape;
	private final StaticPlaneShape groundShape;

	public ChunkPhysics(TerrainWithTr terrain, Player player) {
		this.terrainWithTr = terrain;
		this.player = player;

		this.broadphase = new DbvtBroadphase();
		this.collisionConfiguration = new DefaultCollisionConfiguration();
		this.dispatcher = new CollisionDispatcher(collisionConfiguration);
		this.solver = new SequentialImpulseConstraintSolver();
		this.dynamicsWorld = new DiscreteDynamicsWorld(dispatcher, broadphase, solver, collisionConfiguration);

		this.groundShape = new StaticPlaneShape(new Vector3f(0, 1, 0), 1);
		this.playerShape = new SphereShape(0.5f);

		dynamicsWorld.setGravity(new Vector3f(0, -10, 0));

		DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), new Vector3f(0, -1, 0), 1.0f)));
		RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0,0,0));
		RigidBody groundRigidBody = new RigidBody(groundRigidBodyCI);

		// addComponent our ground to the dynamic world..
		dynamicsWorld.addRigidBody(groundRigidBody);

		Terrain t = terrain.getTerrain();
		Matrix tr = terrainWithTr.getTr();
		for(int x = 0; x<t.getDx(); ++x) {
			for(int y = 0; y<t.getDy(); ++y) {
				for(int z = 0; z<t.getDz(); ++z) {
					if (t.getTerrain(x, y, z) != 0) {
						Vector p = new Vector(x, y, z);
						Vector lowerLeft = tr.times(p);
						Vector topRight = tr.times(p.plus(Vector.ONES));
						Vector center = lowerLeft.times(0.5).plus(topRight.times(0.5));
						Vector halfExtent = topRight.minus(lowerLeft).times(0.5);
						dynamicsWorld.addRigidBody(createCubeBody(center, halfExtent));
					}
				}
			}
		}

	}

	private RigidBody createCubeBody(Vector lowerLeft, Vector halfExtents) {
		BoxShape box = new BoxShape(halfExtents.toVector3f());

		DefaultMotionState groundMotionState = new DefaultMotionState(new Transform(new Matrix4f(new Quat4f(0, 0, 0, 1), lowerLeft.toVector3f(), 1.0f)));
		RigidBodyConstructionInfo groundRigidBodyCI = new RigidBodyConstructionInfo(0, groundMotionState, groundShape, new Vector3f(0,0,0));
		RigidBody rigidBody = new RigidBody(groundRigidBodyCI);

		return rigidBody;
	}



}
