package game.controllers;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.events.Clock;
import game.events.EventBus;
import game.events.TickEvent;
import game.math.Vector;

public class PositionController {

	private static final Logger log = LoggerFactory.getLogger(PositionController.class);

	private Player player;

	private double rx, ry;
	private double vx, vy;
	private double speed;

	private boolean mouseDown;

	public PositionController(EventBus eventBus, Clock clock, InputProcessor inputProcessor, Player player) {
		this.player = player;
		this.rx = 0;
		this.ry = 0;
		this.speed = 10;
		this.mouseDown = false;

		clock.onTick((e) -> onTickEvent(e));
		eventBus.onEvent(inputProcessor, MouseMoveEvent.class, (e) -> onMouseMoveEvent(e));
		eventBus.onEvent(inputProcessor, MouseButtonEvent.class, (e) -> onMouseButtonEvent(e));
		eventBus.onEvent(inputProcessor, KeyDownEvent.class, (e) -> onKeyDownEvent(e));
		eventBus.onEvent(inputProcessor, KeyUpEvent.class, (e) -> onKeyUpEvent(e));
	}

	public void onTickEvent(TickEvent event) {
		double dt = 0.01;
		if (dt != 0 && (vx != 0 || vy != 0)) {
			Vector dp = Vector.U1.times(vx * dt).plus(Vector.U3.times(vy * dt)).times(speed);
			Vector rdp = player.getRotationInv().times(dp);
			player.tryMove(rdp, true);
		}
	}

	public void onMouseMoveEvent(MouseMoveEvent event) {
		if (mouseDown) {
			rotate(event.dx, event.dy);
		}
	}

	public void onMouseButtonEvent(MouseButtonEvent event) {
		if (event.button == 0) {
			mouseDown = event.isDown;
		}
	}

	public void onKeyDownEvent(KeyDownEvent event) {
		if (event.key == Keyboard.KEY_A) {
			vx = -1;
		}
		if (event.key == Keyboard.KEY_D) {
			vx = 1;
		}
		if (event.key == Keyboard.KEY_W) {
			vy = -1;
		}
		if (event.key == Keyboard.KEY_S) {
			vy = 1;
		}
	}

	public void onKeyUpEvent(KeyUpEvent event) {
		if (event.key == Keyboard.KEY_A) {
			vx = 0;
		}
		if (event.key == Keyboard.KEY_D) {
			vx = 0;
		}
		if (event.key == Keyboard.KEY_W) {
			vy = 0;
		}
		if (event.key == Keyboard.KEY_S) {
			vy = 0;
		}
	}

	public Vector getForward() {
		return player.getForward();
	}

	public Vector getPosition() {
		return player.getPosition();
	}

	private void rotate(int drx, int dry) {
		rx += drx;
		ry += dry;
		double sx = rx / 360.0;
		double sy = ry / 360.0;
		player.setRotation(sx, sy);
	}

}
