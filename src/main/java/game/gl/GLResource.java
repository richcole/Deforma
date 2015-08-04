package game.gl;

public abstract class GLResource {
	
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
