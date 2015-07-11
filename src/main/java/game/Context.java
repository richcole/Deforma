package game;

import art.*;
import game.gl.GLDisplay;
import game.gl.GLResourceList;
import game.math.Vector;

public class Context implements Action {
	
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
		this.square = new CompiledMesh(simpleProgram, this.marble, 
				new Mesh().addGeom(new Square(Vector.U3.times(-3), Vector.U1, Vector.U2)));

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
		
//		renderer.add("triangle", triangle);
        renderer.add("view", view);
		renderer.add("square", square);

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
