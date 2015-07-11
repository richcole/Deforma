package art;

import java.util.List;

import com.google.common.collect.Lists;

import game.math.Vector;

public class Mesh {
	List<Geom> geoms = Lists.newArrayList();

	public List<Vector> getVertices() {
		List<Vector> result = Lists.newArrayList();
		for(Geom geom: geoms) {
			result.addAll(geom.getVertices());
		}
		return result;
	}

	public List<Vector> getTexCoords() {
		List<Vector> result = Lists.newArrayList();
		for(Geom geom: geoms) {
			result.addAll(geom.getTexCoords());
		}
		return result;
	}
	
	public List<Integer> getElements() {
		List<Integer> result = Lists.newArrayList();
		int lower = 0;
		for(Geom geom: geoms) {
			List<Integer> elements = geom.getElements(); 
			for(Integer index: elements) {
				result.add(lower + index);
			}
			lower += elements.size();
		}
		return result;
	}

	public Mesh addGeom(Geom geom) {
		geoms.add(geom);
		return this;
	}
}