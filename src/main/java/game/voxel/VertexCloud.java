package game.voxel;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import art.Geom;
import game.math.Vector;

public class VertexCloud implements Geom {
	
	private static final Logger log = LoggerFactory.getLogger(VertexCloud.class);

	List<Vector> vertices = Lists.newArrayList();
	List<Vector> normals = Lists.newArrayList();
	List<Vector> texCoords = Lists.newArrayList();
	List<Integer> elements = Lists.newArrayList();
	List<Integer> bones = Lists.newArrayList();
	
	void addVertex(Vector p, Vector n, Vector texCoord) {
		vertices.add(p);
		normals.add(p);
		texCoords.add(texCoord);
		bones.add(0);
		elements.add(elements.size());
	}

	public List<Vector> getVertices() {
		return vertices;
	}

	public List<Integer> getElements() {
		return elements;
	}

	public List<Vector> getTexCoords() {
		return texCoords;
	}

	public List<Integer> getBones() {
		return bones;
	}
}
