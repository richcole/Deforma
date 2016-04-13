package game.controllers;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputProcessor {

	private static final Logger log = LoggerFactory.getLogger(InputProcessor.class);

	int mx, my, pmx, pmy;

	private EventBus eventBus;

	public InputProcessor(Clock clock, EventBus eventBus) {
		this.pmx = Mouse.getX();
		this.pmy = Mouse.getY();
		this.eventBus = eventBus;
		clock.onTick((e) -> onTick(e));
	}

	public void onTick(TickEvent event) {
		mx = Mouse.getX();
		my = Mouse.getY();

		while (Mouse.next()) {
			int button = Mouse.getEventButton();
			if (button != -1) {
				eventBus.emit(this, new MouseButtonEvent(button, Mouse.getEventButtonState()));
			}
		}

		if ( mx != pmx || my != pmy ) {
		  eventBus.emit(this, new MouseMoveEvent(mx - pmx, my - pmy));
	    pmx = mx;
	    pmy = my;
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				eventBus.emit(this, new KeyDownEvent(Keyboard.getEventKey()));
			} else {
				eventBus.emit(this, new KeyUpEvent(Keyboard.getEventKey()));
			}
		}
	}
}
