package game.controllers;

import game.InputProcessor;
import game.Matrix;
import game.Vector;
import game.View;
import game.events.Clock;
import game.events.EventBus;
import game.events.KeyDownEvent;
import game.events.KeyUpEvent;
import game.events.MouseButtonEvent;
import game.events.MouseMoveEvent;
import game.events.TickEvent;

import org.lwjgl.input.Keyboard;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionController extends InputController {
	
	private static final Logger log = LoggerFactory.getLogger(PositionController.class);
	
	private View   view;
	
	private double rx, ry;
	private double vx, vy;
	private double speed;
	
	private boolean mouseDown;

	Matrix rot1 = Matrix.IDENTITY;
	Matrix rot2 = Matrix.IDENTITY;

	public PositionController(EventBus eventBus, Clock clock, InputProcessor inputProcessor, View view) {
	  super(inputProcessor, eventBus);
		eventBus.onEventType(clock, (e) -> onTickEvent(e), TickEvent.class);

		this.view = view;
    this.rx = 0;
    this.ry = 0;
    this.speed = 10;
    this.mouseDown = false;
	}
	
  public void onTickEvent(TickEvent event) {
    double dt = event.dt;
		if ( dt != 0 && (vx != 0 || vy != 0) ) {
			Vector dp = Vector.U1.times(vx * dt).plus(Vector.U3.times(vy * dt)).times(speed);
			Vector rdp = rot2.times(dp);
			view.move(rdp);
		}
	}

  @Override
	public void onMouseMoveEvent(MouseMoveEvent event) {
		if ( mouseDown ) {
			rotate(event.dx, event.dy);
		}
	}
	
	@Override
	public void onMouseButtonEvent(MouseButtonEvent event) {
		if ( event.button == 0 ) {
			mouseDown = event.isDown;
		}
	}

  @Override
	public void onKeyDownEvent(KeyDownEvent event) {
		if ( event.key == Keyboard.KEY_A) {
			vx = -1;
		}
		if ( event.key == Keyboard.KEY_D) {
			vx = 1;
		}
		if ( event.key == Keyboard.KEY_W) {
			vy = -1;
		}
		if ( event.key == Keyboard.KEY_S) {
			vy = 1;
		}
	}

	@Override
	public void onKeyUpEvent(KeyUpEvent event) {
		if ( event.key == Keyboard.KEY_A) {
			vx = 0;
		}
		if ( event.key == Keyboard.KEY_D) {
			vx = 0;
		}
		if ( event.key == Keyboard.KEY_W) {
			vy = 0;
		}
		if ( event.key == Keyboard.KEY_S) {
			vy = 0;
		}
	}
	
	public Vector getForward() {
		return rot2.times(Vector.U3);
	}
	
	public Vector getPosition() {
		return view.getPosition();
	}

  private void rotate(int drx, int dry) {
    rx += drx;
    ry += dry;
    double sx = rx / 360.0;
    double sy = ry / 360.0;
    
    Matrix rotX1 = Matrix.rot2(sx, Vector.U2);
    Matrix rotX2 = Matrix.rot2(-sx, Vector.U2);
    
    Matrix rotY1 = Matrix.rot2(sy, Vector.U1);
    Matrix rotY2 = Matrix.rot2(-sy, Vector.U1);
    
    rot1 = rotY1.times(rotX1);
    rot2 = rotX2.times(rotY2);
    
    view.setRotation(rot1);
  }

}
