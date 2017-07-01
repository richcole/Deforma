package component;

import com.google.common.collect.Lists;
import component.gl.GlEngine;
import game.gl.GLTexture;
import game.image.BufferedImageImage;
import game.math.Matrix;
import game.math.Vector;
import game.model.CompiledMesh;
import game.model.Mesh;
import game.model.MeshFactory;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.lwjgl.opengl.Display;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

public class UIComponent extends DefaultComponent implements RenderComponent {

	private BufferedImageImage img;
	private GLTexture texture;
	private CompiledMesh compiledMesh;
	private List<Painter> painters = Lists.newArrayList();
	private SimpleClock clock;
	private long lastUpdate = System.currentTimeMillis();

	public void addPainter(Painter painter) {
		painters.add(painter);
	}

	@Value
	@AllArgsConstructor
	public static class PaintContext {
		Graphics2D painter;
		int width, height;
	}

    public UIComponent(GlEngine glEngine, SimpleClock clock) {
		this.lastUpdate = clock.now();
		this.clock = clock;

		img = new BufferedImageImage(new BufferedImage(Display.getWidth(), Display.getHeight(), TYPE_INT_ARGB));
    	texture = new GLTexture(glEngine.getDisposer(), img);
		Mesh mesh = new MeshFactory().newSquareMesh(Vector.M3.times(0.8).plus(Vector.M1.times(0.5)).plus(Vector.M2.times(0.5)), Vector.U1.times(0.5), Vector.U2.times(0.5), "none");
		for(int i=0; i<4; ++i) {
			compiledMesh = glEngine.compileMesh(mesh, texture);
		}
	}

	@Override
	public void render(Scene scene, CameraComponent camera) {
		compiledMesh.render(Matrix.IDENTITY, Matrix.IDENTITY);
	}

	@Override
	public void update(Scene scene) {
		long now = clock.now();
		if (now - lastUpdate > clock.durationOfSeconds(0.1)) {
			repaint(now);
		}
	}

	@Override
	public void init(Scene scene) {
		repaint(clock.now());
	}

	private void repaint(long now) {
		lastUpdate = now;
		PaintContext cxt = new PaintContext(img.getGraphics(), img.getWidth(), img.getHeight());
		clear(cxt);
		for(Painter painter: painters) {
			painter.paint(cxt);
		}
		texture.updateImage(img);
	}

	public void clear(PaintContext cxt) {
		Graphics2D p = cxt.getPainter();
		p.clearRect(0, 0, cxt.getWidth(), cxt.getHeight());
		p.setComposite(AlphaComposite.Src);
		p.setPaint(new Color(0.5f, 0.0f, 0.0f, 0.6f));
		p.fillRect(0, 0, cxt.getWidth(), cxt.getHeight());
	}



}
