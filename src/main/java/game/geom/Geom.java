package game.geom;

import game.TexCoords;
import game.basicgeom.Vector;

import java.util.List;

public interface Geom {
	List<Vector>    getVertices();
	List<Integer>   getElements();
	List<TexCoords> getTexCoords();
	List<Integer>   getBones();
	List<Vector>    getNormals();
}
