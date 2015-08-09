package game.voxel;

public interface InputController {

	void mouseMove(int i, int j);

	void keyDown(int eventKey);

	void keyUp(int eventKey);

	void mouseDown(int button);

	void mouseUp(int button);

}
