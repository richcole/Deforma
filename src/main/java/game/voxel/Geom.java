package game.voxel;

import java.util.List;

public interface Geom {
	List<Vector>  getVertices();
	List<Integer> getElements();
	List<Vector>  getTexCoords();
	List<Integer> getBones();
}
