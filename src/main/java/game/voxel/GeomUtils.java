package game.voxel;

import art.Geom;
import game.math.BBox;
import game.math.Vector;

public class GeomUtils {

	public static BBox getBBox(Geom geom) {
		BBox result = new BBox();
		for (Vector v: geom.getVertices()) {
			result.add(v);
		}
		return result;
	}
}
