package game;

public class GeomUtils {

	public static BBox getBBox(Geom geom) {
		BBox result = new BBox();
		for (Vector v: geom.getVertices()) {
			result.add(v);
		}
		return result;
	}
}
