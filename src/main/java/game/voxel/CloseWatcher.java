package game.voxel;

import org.lwjgl.opengl.Display;

public class CloseWatcher implements Action {

	private boolean isClosed = false;
	
	public void init() {
		isClosed = false;
	}

	public void run() {
		isClosed = Display.isCloseRequested();
	}
	
	public void dispose() {
	}

	public boolean isClosed() {
		return isClosed;
	}

}
