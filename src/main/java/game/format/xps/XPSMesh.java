package game.format.xps;

import java.util.List;

public class XPSMesh {

	String name;
	List<XPSElement> elements;
	List<XPSVertex> vertices;
	List<XPSTexture> textures;
	int numUvLayers;

	public XPSMesh(String name, List<XPSTexture> textures, List<XPSVertex> vertices, List<XPSElement> elements, int numUvLayers) {
		this.name = name;
		this.textures = textures;
		this.vertices = vertices;
		this.elements = elements;
		this.numUvLayers = numUvLayers;
	}

}
