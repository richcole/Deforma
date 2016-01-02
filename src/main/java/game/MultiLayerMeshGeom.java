package game;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class MultiLayerMeshGeom implements Geom {
	
	private static final Logger log = LoggerFactory.getLogger(MultiLayerMeshGeom.class);

	List<Vector> vertices = Lists.newArrayList();
	List<Vector> normals = Lists.newArrayList();
	List<TexCoords> texCoords = Lists.newArrayList();
	List<Integer> elements = Lists.newArrayList();
	List<Integer> bones = Lists.newArrayList();
	
	public MultiLayerMeshGeom() {
	}
	
	public void addVertex(Vector p, Vector n) {
		vertices.add(p);
		normals.add(n);
		bones.add(0);
	}
	
	public void addTexCoord(int index, double u, double v) {
		texCoords.get(index).add(new Vector(u, v, 0, 1.0)); 
	}
	
	public void addMaterial(Material m) {
		texCoords.add(new TexCoords(m));
	}
	
	public void addElement(int a, int b, int c) {
		elements.add(a);
		elements.add(b);
		elements.add(c);
	}

	public List<Vector> getVertices() {
		return vertices;
	}

	public List<Integer> getElements() {
		return elements;
	}

	public List<TexCoords> getTexCoords() {
		return texCoords;
	}

	public List<Integer> getBones() {
		return bones;
	}

	@Override
	public List<Vector> getNormals() {
		return normals;
	}
	
}
