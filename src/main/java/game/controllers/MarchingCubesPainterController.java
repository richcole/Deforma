package game.controllers;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.Box;
import game.CompiledMesh;
import game.InputProcessor;
import game.KdTree;
import game.KdTreeDensityFunction;
import game.Line;
import game.MarchingCubes;
import game.Material;
import game.MeshContainer;
import game.SimpleProgram;
import game.Sphere;
import game.SphericalDensityField;
import game.Utils;
import game.Vector;
import game.events.EventBus;
import game.events.KeyDownEvent;
import game.geom.CubesGeom;
import game.geom.LineGeom;
import game.geom.VertexCloud;

public class MarchingCubesPainterController extends InputController {
	
	private static final Logger log = LoggerFactory.getLogger(MarchingCubesPainterController.class);
	
	private Material material;
	private MeshContainer meshContainer;
	private KdTreeDensityFunction<TerrainPoint> field;
  private KdTree<TerrainPoint> tree;
	private SimpleProgram simpleProgram;
	private boolean wireFrame = false;
	private PositionController posController;
	
	private Vector c;
	private double r;
	private Vector bottomLeft;
	private Vector topRight;
	private Vector res;

  private CubesGeom cubesGeom;

  private EventBus eventBus;

	public MarchingCubesPainterController(EventBus eventBus, PositionController posController, InputProcessor inputProcessor, SimpleProgram simpleProgram, Material material, MeshContainer meshContainer) {
	  super(inputProcessor, eventBus);
	  this.eventBus = eventBus;
		this.posController = posController;
		this.material = material;
		this.tree = new KdTree<TerrainPoint>();
		this.field = new KdTreeDensityFunction<TerrainPoint>(tree);
		this.cubesGeom = new CubesGeom(new VertexCloud(material));
		this.meshContainer = meshContainer;
		this.simpleProgram = simpleProgram;
		this.c = Vector.Z;
		this.r = 10;
		this.bottomLeft = c.minus(Vector.ONES.times(r));
		this.topRight = c.plus(Vector.ONES.times(r));
		this.res = Vector.ONES.times(0.5);

		insertPoint(Vector.U1.times(-1.0), -1, 0.2);
    insertPoint(Vector.U1.times(1.0), 1, 0.2);
    updateModel();
	}
	
	public void updateModel() {
    log.info("Starting Update");

    long begin = System.currentTimeMillis();
    try {
  
      VertexCloud cubesCloud = new VertexCloud(material);
  		MarchingCubes cubes = new MarchingCubes(field, material);
  		cubes.update(cubesCloud, bottomLeft, topRight, res);
  		CompiledMesh cubesMesh = new CompiledMesh(eventBus, simpleProgram, cubesCloud);
      cubesMesh.setWireFrame(wireFrame);
  
      meshContainer.setModel(cubesMesh);
  		meshContainer.setCubesModel(new CompiledMesh(eventBus, simpleProgram, cubesGeom.getVertexCloud()));
    } catch(Exception e) {
      log.error("Exception while updating", e);
    }

		log.info("Time " + (System.currentTimeMillis() - begin));
	}

  @Override
  public void onKeyDownEvent(KeyDownEvent event) {
    if ( event.key == Keyboard.KEY_G) {
      Vector p = posController.getPosition().plus(posController.getForward().normalize().times(2));
      insertPoint(p, 1.0, 0.05);
      updateModel();
    }
    if ( event.key == Keyboard.KEY_B) {
      Vector p = posController.getPosition().plus(posController.getForward().normalize().times(2));
      insertPoint(p, -1.0, 0.05);
      updateModel();
    }
    if ( event.key == Keyboard.KEY_H) {
      wireFrame = ! wireFrame;
      updateModel();
    }
  }

  private void insertPoint(Vector p, double d, double r) {
    tree.insert(p, new TerrainPoint(d));
    cubesGeom.addCube(p, r);
  }
  
}
