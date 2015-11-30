package game.events;


public class AbstractEvent implements Event {
	
	Object object;
	
	public AbstractEvent(Object object) {
		this.object = object;
	}

	@Override
	public Object getObject() {
		return object;
	}

}
