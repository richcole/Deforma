package game;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class HeightMapGeom implements Geom {

	private static final Logger log = LoggerFactory.getLogger(VertexCloud.class);

	List<Vector> vertices = Lists.newArrayList();
	List<Vector> normals = Lists.newArrayList();
	List<TexCoords> texCoords = Lists.newArrayList();
	List<Integer> elements = Lists.newArrayList();
	List<Integer> bones = Lists.newArrayList();

	public HeightMapGeom(Material material, HeightMap heightMap) {
		texCoords = Lists.newArrayList(new TexCoords(material));
		Map<Integer, Integer> vertexIndexMap = Maps.newHashMap();
		for(int z=0;z<heightMap.dz();++z) {
			for(int x=0;x<heightMap.dx();++x) {
				addVertex(heightMap, vertexIndexMap, x, z);
			}
		}

		for(int x=1;x<heightMap.dx();++x) {
			for(int z=1;z<heightMap.dz();++z) {
				int px = x-1;
				int pz = z-1;
				addElement(vertexIndexMap, heightMap, x,  z, px, pz, x,  pz);
				addElement(vertexIndexMap, heightMap, x,  z, px, z,  px, pz);
			}
		}
		
	}

	private void addVertex(HeightMap heightMap, Map<Integer, Integer> vertexIndexMap, int x, int z) {
		vertexIndexMap.put(heightMap.index(x, z), vertices.size());
		vertices.add(heightMap.transform(new Vector(x, heightMap.getHeight(x, z), z, 1.0)));
		normals.add(getNormal(heightMap, x, z));
		texCoords.get(0).add(new Vector(x / (double) heightMap.dx(), z / (double) heightMap.dz(), 0.0, 0.01));
	}

	private Vector getNormal(HeightMap heightMap, int x, int z) {
		Vector n = Vector.Z;
		double dxdy = 0;
		double dzdy = 0;
		double dx = 0;
		double dz = 0;
		if ( x > 0 ) {
			dxdy += heightMap.getHeight(x, z) - heightMap.getHeight(x-1, z);
			dx += 1;
		}
		if ( x+1 < heightMap.dx() ) {
			dxdy += heightMap.getHeight(x+1, z) - heightMap.getHeight(x, z);
			dx += 1;
		}		
		if ( z > 0 ) {
			dzdy += heightMap.getHeight(x, z) - heightMap.getHeight(x, z-1);
			dz += 1;
		}
		if ( z+1 < heightMap.dz() ) {
			dzdy = heightMap.getHeight(x, z+1) - heightMap.getHeight(x, z);
			dz += 1;
		}		
		return new Vector(dxdy / dx, -1.0, dzdy / dz, 1.0).normalize();
	}

	private void addElement(Map<Integer, Integer> vertexIndexMap, HeightMap heightMap, int x1, int z1, int x2, int z2, int x3, int z3) {
		elements.add(vertexIndexMap.get(heightMap.index(x1, z1)));
		elements.add(vertexIndexMap.get(heightMap.index(x2, z2)));
		elements.add(vertexIndexMap.get(heightMap.index(x3, z3)));
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
