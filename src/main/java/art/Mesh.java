package art;

public class Mesh extends GeomList {
	
	private Material material;

	public Mesh(Material material) {
		super();
		this.material = material;
	}

	public Material getMaterial() {
		return material;
	}
	
}
