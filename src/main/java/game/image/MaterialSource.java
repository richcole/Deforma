package game.image;

import game.events.EventBus;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

public class MaterialSource {
	
	private Map<String, Material> materials = Maps.newHashMap();
  private EventBus eventBus;

	public MaterialSource(EventBus eventBus) {
	  this.eventBus = eventBus;
	}

	public Material get(File root, String name) {
		Material material = materials.get(name);
		if ( material == null ) {
			material = new ImageTexture(eventBus, new File(root, name));
			materials.put(name, material);
		}
		return material;
	}
}
