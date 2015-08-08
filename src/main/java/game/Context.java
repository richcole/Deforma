package game;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import art.CompiledMesh;
import art.CompiledMeshList;
import art.ImageTexture;
import art.Material;
import art.Mesh;
import art.SimpleProgram;
import art.TriangleMesh;
import game.gl.GLDisplay;
import game.gl.GLResourceList;
import game.math.Matrix;
import game.math.Vector;
import game.voxel.MarchingCubes;
import game.voxel.SphericalDensityField;
import game.voxel.VertexCloud;
import io.ModelLoader;

public class Context implements Action {
	
	private static final Logger log = LoggerFactory.getLogger(Context.class);
	
	private ActionList actionList;
	private CloseWatcher closeWatcher;
	private SimpleProgram simpleProgram;
	private GLDisplay display;
	private GLResourceList resourceList;
	private DisplayResizer dispayResizer;
	private TriangleMesh triangle;
    private View view;
    private Material gradientTexture;
    private Material marble;
	private Simulator simulator;
	private InputProcessor inputProcessor;
	private PositionController positionController;
	private CompiledMesh cubesMesh;

    Context() {
	}
	
	public void init() {
		this.actionList = new ActionList(this);
		this.resourceList = new GLResourceList();

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

		SphericalDensityField field = new SphericalDensityField();
		field.add(Vector.U1.times(-2), 0.6);
		field.add(Vector.U1.times(2), 0.6);
		
		MarchingCubes cubes = new MarchingCubes(field, this.marble);
		cubes.update(cubesCloud, p.minus(dp), p.plus(dp), Vector.ONES.times(0.5));

		Mesh mesh = new Mesh(this.marble);
		mesh.addGeom(cubesCloud);
		cubesMesh = new CompiledMesh(simpleProgram, mesh);
		cubesMesh.setWireFrame(true);

		CompiledMeshList girl = new CompiledMeshList(simpleProgram, 
				new ModelLoader().load("/home/richcole/models/Girl/girl.3ds", marble));
		girl.setModelTr(Matrix.rot(Math.PI / 2, Vector.U1));
		
		CompiledMeshList trees = new CompiledMeshList(simpleProgram, 
				new ModelLoader().load("/home/richcole/models/trees9/trees9.3ds", marble));
		trees.setModelTr(Matrix.translate(new Vector(20, 0, 0)).times(Matrix.rot(Math.PI / 2, Vector.U1)));
		
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
		actionList.add("renderer", view);
		
		resourceList.addResource(display);
		resourceList.addResource(simpleProgram);
		resourceList.addResource(triangle);
		resourceList.addResource(girl);
		resourceList.addResource(trees);
		resourceList.addResource(cubesMesh);
		
		view.add(triangle);
		view.add(girl);
		view.add(cubesMesh);
		view.add(trees);

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
