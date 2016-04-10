package game;

import game.basicgeom.Vector;
import game.image.Material;

import java.util.List;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;

public class TexCoords {
	
	Material material;
	List<Vector> coords;
	
	public TexCoords(Material material) {
		super();
		this.material = material;
		this.coords = Lists.newArrayList();
	}
	
	public void add(Vector v) {
		coords.add(v);
	}

	public Vector get(int i) {
		try {
			return coords.get(i);
		} catch(Exception e) {
			Throwables.propagate(e);
			return null;
		}
	}

	public Material getMaterial() {
		return material;
	}

}
