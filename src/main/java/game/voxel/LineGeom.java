package game.voxel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LineGeom extends MeshGeom {

    final static Logger log = LoggerFactory.getLogger(LineGeom.class);

	private Box box;
		
	public LineGeom(Box box, double width) {
		this.box = box;
		
		Vector dp = box.dp().normalize();
		Vector n1 = dp.cross(Vector.U1).times(width);
		if ( n1.lengthSquared() < 0.01 ) {
			n1 = dp.cross(Vector.U2).times(width);
		}
		Vector n2 = dp.cross(n1);
		Vector hdp = box.dp().times(0.5);
		Vector c = box.center();

		log.info("n1.length " + n1.length() + " n2.length() " + n2.length());
		log.info("c " + c + " hdp " + hdp + " n1 " + n1 + " n2 " + n2);
		addCube(box.bottomLeft, n1, n2);
		addCube(box.topRight, n1, n2);

		addCube(c.plus(n1), hdp, n2);
		addCube(c.minus(n1), hdp, n2);
		addCube(c.plus(n2), hdp, n1);
		addCube(c.minus(n2), hdp, n1);
	}
	
	public void addCube(Vector center, Vector up, Vector right) {
		log.info("Add Cube " + center + " " + up + " " + right);
		log.info(" center+up " + center.plus(up) + " center+right " + center.plus(right));
		
		Vector n = up.cross(right).normalize();
		addVertex(center.plus(up).plus(right), n);    // right up
		elements.add(vertices.size()-1);
		
		addVertex(center.minus(up).plus(right), n);   // right
		elements.add(vertices.size()-1);
		
		addVertex(center.plus(up).minus(right), n);   // up
		elements.add(vertices.size()-1);
		
		addVertex(center.minus(up).plus(right), n);   // right
		elements.add(vertices.size()-1);
		
		addVertex(center.minus(up).minus(right), n);  // 
		elements.add(vertices.size()-1);
		
		addVertex(center.plus(up).minus(right), n);   // up
		elements.add(vertices.size()-1);
	}
	
	private void addVertex(Vector p, Vector n) {
		double tx = p.x() - box.bottomLeft.x() / (box.topRight.x() - box.bottomLeft.x());
		double ty = p.y() - box.bottomLeft.y() / (box.topRight.y() - box.bottomLeft.y());
		addVertex(p, n, new Vector(tx, ty, 0, 1.0));
	}

}