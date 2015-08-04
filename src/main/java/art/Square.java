package art;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import game.Utils;
import game.math.Vector;

public class Square implements Geom {

    final static Logger log = LoggerFactory.getLogger(Square.class);

    private Vector center, up, right;
	
	public Square(Vector center, Vector right, Vector up) {
		this.center = center;
		this.up = up;
		this.right = right;
	}
	
	public List<Vector> getVertices() {
		List<Vector> result = Lists.newArrayList();
		
		result.add(center.plus(up).plus(right));    // right up
		result.add(center.minus(up).plus(right));   // right
		result.add(center.plus(up).minus(right));   // up
		result.add(center.minus(up).plus(right));   // right
		result.add(center.minus(up).minus(right));  // 
		result.add(center.plus(up).minus(right));   // up

        log.info("square verts " + result);

		return result;
	}
	
	public List<Vector> getTexCoords() {
		List<Vector> result = Lists.newArrayList();
		result.add(Vector.U1.plus(Vector.U2));
		result.add(Vector.U1);
		result.add(Vector.U2);

		result.add(Vector.U1);
		result.add(Vector.Z);
		result.add(Vector.U2);

        log.info("square texCoords " + result);

        return result;
	}

	public List<Integer> getElements() {
		return Utils.range(0, 6);
	}

	public List<Integer> getBones() {
		return Lists.newArrayList(1);
	}
}
