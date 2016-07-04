package game.controllers;

import game.math.Matrix;
import game.math.Vector;
import game.view.View;

public class Player {
	
	private View view;
	private TerrainPlayerController terrainPlayerController;

	public Player(View view) {
		this.view = view;
	}
	
	public void setTerrainPlayerController(TerrainPlayerController terrainPlayerController) {
		this.terrainPlayerController = terrainPlayerController;
	}
	
	public void move(Vector dx) {
		view.move(dx);
	}
	
	public void tryMove(Vector dx) {
		if ( terrainPlayerController != null ) {
			dx = terrainPlayerController.tryMove(getPosition(), dx);
			view.move(dx);
		}
		else {
			view.move(dx);
		}
	}

	public Matrix getRotationInv() {
		return view.getRotationInv();
	}

	public Vector getForward() {
		return view.getForward();
	}

	public Vector getPosition() {
		return view.getPosition();
	}

	public Vector getUp() {
		return view.getUp();
	}

	public void setRotation(double sx, double sy) {
		view.setRotation(sx, sy);
	}

	public Vector getLeft() {
		return view.getLeft();
	}

}
