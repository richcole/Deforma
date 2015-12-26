package game.events;

import game.Registration;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

import junit.framework.Assert;

public class TestEventBus {
	
	static public class ExampleEvent extends AbstractEvent {

		public ExampleEvent(Object object) {
			super(object);
		}
		
	}
	
	@Test
	public void test() {
		EventBus bus = new EventBus();
		Object object = new Object();
		List<String> results = Lists.newArrayList();
		Registration<ExampleEvent> reg = bus.onEventType(object, (ExampleEvent event) -> { results.add("one"); }, ExampleEvent.class);
		Assert.assertEquals(results.size(), 0);
		bus.post(new ExampleEvent(object));
		Assert.assertEquals(results.size(), 1);
		bus.unregister(reg);
		bus.post(new ExampleEvent(object));
		Assert.assertEquals(results.size(), 1);
	}

}
