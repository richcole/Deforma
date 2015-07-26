package game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import art.*;
import game.gl.GLDisplay;
import game.gl.GLResourceList;
import game.math.BBox;
import game.math.Vector;
import game.voxel.GeomUtils;
import game.voxel.MarchingCubes;
import game.voxel.MeshGeom;
import game.voxel.SphericalDensityFunction;
import game.voxel.VertexCloud;
import io.ModelLoader;

public class Context implements Action {
	
	private static final Logger log = LoggerFactory.getLogger(Context.class);
	
	private ActionList actionList;
	private Renderer renderer;
	private CloseWatcher closeWatcher;
	private SimpleProgram simpleProgram;
	private GLDisplay display;
	private GLResourceList resourceList;
	private DisplayResizer dispayResizer;
	private TriangleMesh triangle;
    private CompiledMesh square;
    private View view;
    private ImageTexture gradientTexture;
    private ImageTexture marble;
	private Simulator simulator;
	private InputProcessor inputProcessor;
	private PositionController positionController;
	private CompiledMesh cubesMesh;
	private MeshGeom model;
	

    Context() {
	}
	
	public void init() {
		this.actionList = new ActionList(this);
		this.resourceList = new GLResourceList();

		this.renderer = new Renderer();
		this.closeWatcher = new CloseWatcher();
		this.simpleProgram = new SimpleProgram();
		this.display = new GLDisplay();
		this.dispayResizer = new DisplayResizer();
		this.marble = new ImageTexture("skyline.jpg");
        this.gradientTexture = new ImageTexture(new GradientImage(256, 256));
		this.triangle = new TriangleMesh(simpleProgram, this.gradientTexture);
		
		VertexCloud cubesCloud = new VertexCloud();
		Vector p = new Vector(0, 0, 0);
		Vector dp = new Vector(1, 1, 1).times(6); 
		MarchingCubes cubes = new MarchingCubes(new SphericalDensityFunction(p, 5));
		cubes.update(cubesCloud, p.minus(dp), p.plus(dp));
		cubesMesh = new CompiledMesh(simpleProgram, this.marble, new Mesh().addGeom(cubesCloud));

		this.square = new CompiledMesh(simpleProgram, this.marble, 
				new Mesh().addGeom(new Square(p.minus(dp), Vector.U1.times(1), Vector.U2.times(1))));
		
		this.model = new ModelLoader().load("/home/richcole/models/Girl/girl.3ds");
		BBox bbox = GeomUtils.getBBox(this.model);
		log.info("bbox " + bbox);
		this.square = new CompiledMesh(simpleProgram, this.marble, new Mesh().addGeom(model));
		
		this.view = new View(simpleProgram);
        this.simulator = new Simulator();
        this.inputProcessor = new InputProcessor();
        this.positionController = new PositionController(view);

        inputProcessor.add(positionController);
        simulator.add(positionController);

		actionList.add("closeWatcher", closeWatcher);
		actionList.add("displayResizer", dispayResizer);
		actionList.add("inputProcessor", inputProcessor);
		actionList.add("simulator", simulator);
		actionList.add("renderer", renderer);
		
		resourceList.add("display", display);
		resourceList.add("simpleProgram", simpleProgram);
		resourceList.add("triangle", triangle);
		resourceList.add("marble", marble);
		resourceList.add("square", square);
        resourceList.add("gradientTexture", gradientTexture);
        resourceList.add("cubeMesh", cubesMesh);
		
//		renderer.add("triangle", triangle);
        renderer.add("view", view);
		renderer.add("square", square);
		renderer.add("cubeMesh", cubesMesh);

		resourceList.init();
		actionList.init();	
	}
	
	public void dispose() {
		actionList.dispose();
		resourceList.dispose();
	}

	public void run() {
		actionList.run();
	}

	public CloseWatcher getCloseWatcher() {
		return closeWatcher;
	}

}
