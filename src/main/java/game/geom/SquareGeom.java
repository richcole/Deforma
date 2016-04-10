package game.geom;

import game.TexCoords;
import game.Utils;
import game.basicgeom.Vector;
import game.image.Material;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

public class SquareGeom implements Geom {

	final static Logger log = LoggerFactory.getLogger(SquareGeom.class);

	private Vector center, up, right;

	private TexCoords texCoords;

	public SquareGeom(Vector center, Vector right, Vector up, Material material) {
		this.center = center;
		this.up = up;
		this.right = right;

		texCoords = new TexCoords(material);

		texCoords.add(Vector.U1.plus(Vector.U2));
		texCoords.add(Vector.U1);
		texCoords.add(Vector.U2);

		texCoords.add(Vector.U1);
		texCoords.add(Vector.Z);
		texCoords.add(Vector.U2);
	}

	public List<Vector> getVertices() {
		List<Vector> result = Lists.newArrayList();

		result.add(center.plus(up).plus(right)); // right up
		result.add(center.minus(up).plus(right)); // right
		result.add(center.plus(up).minus(right)); // up
		result.add(center.minus(up).plus(right)); // right
		result.add(center.minus(up).minus(right)); //
		result.add(center.plus(up).minus(right)); // up

		return result;
	}

	public List<Vector> getNormals() {
		Vector n = up.cross(right).normalize();
		List<Vector> result = Lists.newArrayList();
		for(int i=0;i<6;++i) {
			result.add(n);
		}
		return result;
	}
	
	public List<TexCoords> getTexCoords() {
		return Lists.newArrayList(texCoords);
	}

	public List<Integer> getElements() {
		return Utils.range(0, 6);
	}

	public List<Integer> getBones() {
		return Lists.newArrayList(1);
	}

}
