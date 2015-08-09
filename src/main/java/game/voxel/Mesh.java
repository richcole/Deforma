package game.voxel;

public class Mesh extends GeomList {
	
	private Material material;

	public Mesh(Material material, Geom ... geoms) {
		this(material);
		for(Geom geom: geoms) {
			addGeom(geom);
		}
	}
	
	public Mesh(Material material) {
		super();
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}
	
}
