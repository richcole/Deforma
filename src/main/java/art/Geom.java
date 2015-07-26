package art;

import java.util.List;

import game.math.Vector;

public interface Geom {
	
	List<Vector>  getVertices();
	List<Integer> getElements();
	List<Vector>  getTexCoords();
	List<Integer> getBones();
}
