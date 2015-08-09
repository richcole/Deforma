package game.voxel;

import java.util.List;

import com.google.common.collect.Lists;

public class GeomList {
		private List<Geom> geoms = Lists.newArrayList();
		
		public GeomList() {
		}

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

		public List<Integer> getBones() {
			List<Integer> result = Lists.newArrayList();
			int lower = 0;
			int upper = 0;
			for(Geom geom: geoms) {
				List<Integer> boneIndexes = geom.getBones(); 
				for(Integer index: boneIndexes) {
					result.add(lower + index);
					upper = Math.max(lower + index, upper);
				}
				lower = upper;
			}
			return result;
		}

		public GeomList addGeom(Geom geom) {
			geoms.add(geom);
			return this;
		}

}
