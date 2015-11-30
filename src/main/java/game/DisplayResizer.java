package game;

import java.util.function.Consumer;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DisplayResizer implements Consumer<TickEvent> {
	
	final static Logger log = LoggerFactory.getLogger(DisplayResizer.class);
	
	DisplayResizer(Clock clock, EventBus eventBus) {
	  eventBus.onEventType(clock, this, TickEvent.class);
	}

	public void accept(TickEvent event) {
		if (Display.wasResized()) {
	    log.info("Resize display " + Display.getWidth() + " " + Display.getHeight());
	    GL11.glViewport(0, 0, Display.getWidth(), Display.getHeight());
		}
	}

}
