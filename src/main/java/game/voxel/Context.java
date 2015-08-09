package game.voxel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

		this.view = new View(simpleProgram);

		MeshContainer meshContainer = new MeshContainer();
		PositionController positionController = new PositionController(view);
		MarchingCubesPainterController mcPainterController = new MarchingCubesPainterController(positionController, simpleProgram, marble, meshContainer);

		CompiledMeshList girl = new CompiledMeshList(simpleProgram, 
				new ModelLoader().load("/home/richcole/models/Girl/girl.3ds", marble));
		girl.setModelTr(Matrix.rot(Math.PI / 2, Vector.U1));
		
		CompiledMeshList trees = new CompiledMeshList(simpleProgram, 
				new ModelLoader().load("/home/richcole/models/trees9/trees9.3ds", marble));
		trees.setModelTr(Matrix.translate(new Vector(20, 0, 0)).times(Matrix.rot(Math.PI / 2, Vector.U1)));
		
		LineGeom line = new LineGeom(new Box(Vector.U1.times(-5), Vector.U2.times(5)), 0.2);
		CompiledMesh lineMesh = new CompiledMesh(simpleProgram, new Mesh(marble, line));
		
        this.simulator = new Simulator();
        this.inputProcessor = new InputProcessor();

        inputProcessor.add(positionController);
        inputProcessor.add(mcPainterController);
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
		resourceList.addResource(lineMesh);
		
		view.add(triangle);
		view.add(girl);
		view.add(meshContainer);
		view.add(trees);
		view.add(lineMesh);

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
