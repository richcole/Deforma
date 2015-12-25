package game;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

public class XPSReader {

	BReader reader;
	File root;
	
	public XPSReader() {
	}
	
	public XPSModel read(File root, String name) {
		this.root = root;
		reader = new BReader(new File(root, name));

		long numBones = reader.readu32();
		List<XPSMesh> meshes;
		List<XPSBone> bones;
		if ( numBones == 323232 ) {
			long version = reader.readu32();
			String modelAuthor = reader.readXPSString();
			long numSomething = reader.readu32();
			String s1 = reader.readXPSString();
			String s2 = reader.readXPSString();
			String s3 = reader.readXPSString();
			for(int i=0;i<numSomething;++i) {
				reader.readu32();
			}
			boolean hasTangents = version != 0x000f0002;
			numBones = reader.readu32();
			bones = readBones(numBones);
			meshes = readMeshes(hasTangents);		
			return new XPSModel(root, bones, meshes);
		}
		else {
			boolean hasTangents = true;
			bones = readBones(numBones);
			meshes = readMeshes(hasTangents);
			return new XPSModel(root, bones, meshes);
		}
	}

	private List<XPSBone> readBones(long numBones) {
		List<XPSBone> bones = Lists.newArrayList();
		for(int i=0;i<numBones;++i) {
			bones.add(readBone());
		}
		return bones;
	}

	private XPSBone readBone() {
		return new XPSBone(reader.readXPSString(), reader.readu16(), readVector3f());
	}

	private Vector readVector3f() {
		return new Vector(reader.readFloat(), reader.readFloat(), reader.readFloat());
	}

	private List<XPSMesh> readMeshes(boolean hasTangents) {
		long numMeshes = reader.readu32();
		List<XPSMesh> meshes = Lists.newArrayList();
		for(int i=0;i<numMeshes;++i) {
			meshes.add(readMesh(hasTangents));
		}
		return meshes;
	}

	private XPSMesh readMesh(boolean hasTangents) {
		String name = reader.readXPSString();
		int numUvLayers = (int) reader.readu32();
		List<XPSTexture> textures = readTextures();
		List<XPSVertex> vertices = readVertices(numUvLayers, hasTangents);
		List<XPSElement> elements = readElements();
		return new XPSMesh(name, textures, vertices, elements, numUvLayers);
	}

	private List<XPSVertex> readVertices(int numUvLayers, boolean hasTangents) {
		long numVertices = reader.readu32();
		List<XPSVertex> vertices = Lists.newArrayList();
		for(int i=0;i<numVertices;++i) {
			vertices.add(readVertex(numUvLayers, hasTangents));
		}
		return vertices;
		
	}

	private List<XPSTexture> readTextures() {
		long numTextures = reader.readu32();
		List<XPSTexture> textures = Lists.newArrayList();
		for(int i=0;i<numTextures;++i) {
			textures.add(readTexture());
		}
		return textures;
	}

	private List<XPSElement> readElements() {
		long numElements = reader.readu32();
		List<XPSElement> elements = Lists.newArrayList();
		for(int i=0;i<numElements;++i) {
			elements.add(readElement());
		}
		return elements;
	}

	private XPSTexture readTexture() {
		String name = reader.readXPSString();
		long layerId = reader.readu32();
		return new XPSTexture(name, layerId);
	}

	private XPSVertex readVertex(int numUvLayers, boolean hasTangents) {
		Vector v = readVector3f();
		Vector n = readVector3f();
		Vector c = readVector4c();
		float[] st = reader.readFloats(numUvLayers * 2);
		float[] tangents = null;
		if ( hasTangents ) {
			tangents = reader.readFloats(numUvLayers * 4);
		}
		int[] boneIndexes = reader.readu16s(4);
		float[] boneFloats = reader.readFloats(4);
		return new XPSVertex(v, n, c, st, tangents, boneIndexes, boneFloats);
	}

	private Vector readVector4c() {
		return new Vector(reader.readu8(), reader.readu8(), reader.readu8(), reader.readu8());
	}

	private XPSElement readElement() {
		int a = (int)reader.readu32();
		int b = (int)reader.readu32();
		int c = (int)reader.readu32();
		return new XPSElement(a, b, c);
	}
}
