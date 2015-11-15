package game;

import java.util.List;
import java.util.Set;

public interface Geom {
	List<Vector>    getVertices();
	List<Integer>   getElements();
	List<TexCoords> getTexCoords();
	List<Integer>   getBones();
}
