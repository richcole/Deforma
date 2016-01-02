package game.geom;

import game.Box;
import game.Material;
import game.Vector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LinesGeom extends SingleLayerMeshGeom {

    final static Logger log = LoggerFactory.getLogger(LinesGeom.class);

	public LinesGeom(Material material) {
		super(material);
	}
	
	public void addLine(Vector from, Vector to, double width) {
		Box box = new Box(from, to);
		Vector dp = box.dp().normalize();
		Vector n1 = dp.cross(Vector.U1).times(width);
		if ( n1.lengthSquared() < 0.01 ) {
			n1 = dp.cross(Vector.U2).times(width);
		}
		Vector n2 = dp.cross(n1);
		Vector hdp = box.dp().times(0.5);
		Vector c = box.center();

		addFace(box, box.bottomLeft, n1, n2);
		addFace(box, box.topRight, n1, n2);

		addFace(box, c.plus(n1), hdp, n2);
		addFace(box, c.minus(n1), hdp, n2);
		addFace(box, c.plus(n2), hdp, n1);
		addFace(box, c.minus(n2), hdp, n1);
	}
	
	public void addFace(Box box, Vector center, Vector up, Vector right) {
		
		Vector n = up.cross(right).normalize();
		addVertex(box, center.plus(up).plus(right), n);    // right up
		elements.add(vertices.size()-1);
		
		addVertex(box, center.minus(up).plus(right), n);   // right
		elements.add(vertices.size()-1);
		
		addVertex(box, center.plus(up).minus(right), n);   // up
		elements.add(vertices.size()-1);
		
		addVertex(box, center.minus(up).plus(right), n);   // right
		elements.add(vertices.size()-1);
		
		addVertex(box, center.minus(up).minus(right), n);  // 
		elements.add(vertices.size()-1);
		
		addVertex(box, center.plus(up).minus(right), n);   // up
		elements.add(vertices.size()-1);
	}
	
	private void addVertex(Box box, Vector p, Vector n) {
		double tx = p.x() - box.bottomLeft.x() / (box.topRight.x() - box.bottomLeft.x());
		double ty = p.y() - box.bottomLeft.y() / (box.topRight.y() - box.bottomLeft.y());
		addVertex(p, n, new Vector(tx, ty, 0, 1.0));
	}

}
