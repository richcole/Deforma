package game;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

public class MaterialSource {
	
	Map<String, Material> materials = Maps.newHashMap();

	public MaterialSource() {
	}

	public Material get(File root, String name) {
		Material material = materials.get(name);
		if ( material == null ) {
			material = new ImageTexture(new File(root, name));
			materials.put(name, material);
		}
		return material;
	}
}
