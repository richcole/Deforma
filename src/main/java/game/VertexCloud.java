package game;

import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class VertexCloud implements Geom {
	
	private static final Logger log = LoggerFactory.getLogger(VertexCloud.class);

	List<Vector> vertices = Lists.newArrayList();
	List<Vector> normals = Lists.newArrayList();
	List<TexCoords> texCoords = Lists.newArrayList();
	List<Integer> elements = Lists.newArrayList();
	List<Integer> bones = Lists.newArrayList();
	
	public VertexCloud(Material material) {
		texCoords = Lists.newArrayList(new TexCoords(material));
	}
	
	public void addVertex(Vector p, Vector n, Vector texCoord) {
		vertices.add(p);
		normals.add(n);
		texCoords.get(0).add(texCoord);
		bones.add(0);
		elements.add(elements.size());
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
