package game.controllers;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.Box;
import game.CompiledMesh;
import game.InputProcessor;
import game.Line;
import game.LineGeom;
import game.MarchingCubes;
import game.Material;
import game.MeshContainer;
import game.SimpleProgram;
import game.Sphere;
import game.SphericalDensityField;
import game.Utils;
import game.Vector;
import game.VertexCloud;
import game.events.EventBus;
import game.events.KeyDownEvent;

public class MarchingCubesPainterController extends InputController {
	
	private static final Logger log = LoggerFactory.getLogger(MarchingCubesPainterController.class);
	
	private Material material;
	private MeshContainer meshContainer;
	private SphericalDensityField field;
	private SimpleProgram simpleProgram;
	private boolean wireFrame = false;
	private PositionController posController;
	
	Vector c;
	double r;
	Vector bottomLeft;
	Vector topRight;
	Vector res;

	public MarchingCubesPainterController(EventBus eventBus, PositionController posController, InputProcessor inputProcessor, SimpleProgram simpleProgram, Material material, MeshContainer meshContainer) {
	  super(inputProcessor, eventBus);
		this.posController = posController;
		this.material = material;
		this.field = new SphericalDensityField();
		this.meshContainer = meshContainer;
		this.simpleProgram = simpleProgram;
		this.c = Vector.Z;
		this.r = 12;
		this.bottomLeft = c.minus(Vector.ONES.times(r));
		this.topRight = c.plus(Vector.ONES.times(r));
		this.res = Vector.ONES.times(0.5);

		field.add(Vector.U1.times(-2), 0.6);
		field.add(Vector.U1.times(2), 0.6);
	}
	
	public void tick(long dt) {
	}
	
	public void updateModel() {
		VertexCloud cubesCloud = new VertexCloud(material);
		
		long begin = System.currentTimeMillis();
		MarchingCubes cubes = new MarchingCubes(field, material);
		cubes.update(cubesCloud, bottomLeft, topRight, res);

		CompiledMesh cubesMesh = new CompiledMesh(simpleProgram, cubesCloud);
		cubesMesh.setWireFrame(wireFrame);

		meshContainer.setModel(cubesMesh);

		log.info("Time " + (System.currentTimeMillis() - begin));
	}

	@Override
	public void onKeyDownEvent(KeyDownEvent event) {
		if ( event.key == Keyboard.KEY_G) {
			Sphere sp = new Sphere(c, r);
			Line line = new Line(posController.getPosition(), posController.getForward());
			setLineMesh(new Box(posController.getPosition(), posController.getPosition().plus(posController.getForward())));
			log.info("Sphere " + sp + " Line " + line);
			Box box = Utils.intersection(sp, line);
			if ( box != null ) {
				box = box.orientTo(posController.getForward());
				log.info("Pos " + posController.getPosition() + " Box " + box);
				Vector p = field.intersection(box.bottomLeft, box.topRight, box.dp().normalize().times(0.5));
				if ( p != null ) {
					log.info("p " + p);
					field.add(p, 0.1);
					updateModel();
				}
			}
		}
		if ( event.key == Keyboard.KEY_H) {
			wireFrame = ! wireFrame;
			updateModel();
		}
	}

	private void setLineMesh(Box box) {
		LineGeom line = new LineGeom(box, 0.1, material);
		CompiledMesh lineModel = new CompiledMesh(simpleProgram, line);
		meshContainer.setLineModel(lineModel);
	}

}
