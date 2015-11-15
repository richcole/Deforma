package game;

public abstract class AbstractGLResource implements GLResource {
	
	boolean initialized = false;
	
	public void ensureInitialized() {
		if ( ! initialized ) {
			initialized = true;
			init();
		}
	}
	
	public abstract void init();
	public abstract void dispose();

}
