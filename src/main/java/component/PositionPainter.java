package component;

import game.math.Vector;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

public class PositionPainter implements Painter {

	private UIComponent uiComponent;
	private PhysicalObject physicalObject;

	private int x, y;
	private int fontSize = 22;
	private int lineSkip = 22;


	public PositionPainter(UIComponent uiComponent, PhysicalObject physicalObject) {
		this.physicalObject = physicalObject;
		this.uiComponent = uiComponent;
	}

	@Override
	public void paint(UIComponent.PaintContext paintContext) {
		Graphics2D p = paintContext.getPainter();
		Vector pos = physicalObject.getP();
		Vector fwd = physicalObject.getFwd();
		x = 10;
		y = 10 + lineSkip;
		p.setColor(Color.WHITE);
		p.setFont(new Font("TimesRoman", Font.PLAIN, fontSize));
		printLn(p, "fwd: %.2f %.2f %.2f", fwd.x(), fwd.y(), fwd.z());
		printLn(p, "pos: %.2f %.2f %.2f", pos.x(), pos.y(), pos.z());
	}

	public void printLn(Graphics2D p, String fmt, Object ... objs) {
		p.drawString(String.format(fmt, objs), x, y);
		y += lineSkip;
	}
}
