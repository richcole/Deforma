package game;

import game.events.Clock;
import game.events.EventBus;
import game.events.KeyDownEvent;
import game.events.KeyUpEvent;
import game.events.MouseButtonEvent;
import game.events.MouseMoveEvent;
import game.events.TickEvent;

import java.util.List;
import java.util.function.Consumer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class InputProcessor implements Consumer<TickEvent> {

	private static final Logger log = LoggerFactory.getLogger(Context.class);

	int mx, my, pmx, pmy;

	private EventBus eventBus;

	public InputProcessor(Clock clock, EventBus eventBus) {
		this.pmx = Mouse.getX();
		this.pmy = Mouse.getY();
		this.eventBus = eventBus;
		eventBus.onEventType(clock, this, TickEvent.class);
	}

	public void accept(TickEvent event) {
		mx = Mouse.getX();
		my = Mouse.getY();

		while (Mouse.next()) {
			int button = Mouse.getEventButton();
			if (button != -1) {
				eventBus.post(
						new MouseButtonEvent(this, button, Mouse.getEventButtonState()));
			}
		}

		eventBus.post(new MouseMoveEvent(this, mx - pmx, my - pmy));
		pmx = mx;
		pmy = my;

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				eventBus.post(new KeyDownEvent(this, Keyboard.getEventKey()));
			} else {
				eventBus.post(new KeyUpEvent(this, Keyboard.getEventKey()));
			}
		}
	}
}
