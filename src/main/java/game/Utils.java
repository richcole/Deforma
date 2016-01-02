package game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.google.common.collect.Lists;

public class Utils {

	public static FloatBuffer toFloatBuffer(float[] floats) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(floats.length).put(floats);
		buf.flip();
		return buf;
	}

	public static IntBuffer toIntBuffer(int[] ints) {
		IntBuffer buf = BufferUtils.createIntBuffer(ints.length).put(ints);
		buf.flip();
		return buf;
	}

	public static IntBuffer toIntBuffer(List<Integer> ints) {
		IntBuffer buf = BufferUtils.createIntBuffer(ints.size());
		for(Integer value: ints) {
			buf.put(value);
		}
		buf.flip();
		return buf;
	}

	public static FloatBuffer toFloatBuffer(List<Float> ints) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(ints.size());
		for(Float value: ints) {
			buf.put(value);
		}
		buf.flip();
		return buf;
	}

	public static ByteBuffer toByteBuffer(byte[] bytes) {
		ByteBuffer buf = BufferUtils.createByteBuffer(bytes.length).put(bytes);
		return buf;
	}

	public static ByteBuffer toByteBuffer(String name) {
		byte[] bytes = name.getBytes();
		ByteBuffer buf = BufferUtils.createByteBuffer(bytes.length+1);
		buf.put(bytes);
		buf.put((byte)0);
		buf.flip();
		return buf;
	}
	
	public static List<Integer> range(int lower, int upper) {
		List<Integer> result = Lists.newArrayList();
		for(int i=lower;i<upper;++i) {
			result.add(i);
		}
		return result;
	}

	public static List<Integer> range(int upper) {
		return range(0, upper);
	}

	public static double clamp(double v, double l, double u) {
		if (v < l) {
			return l;
		}
		if (v > u) {
			return u;
		}
		return v;
	}
	
	public static double mix(double x1, double x2, double alpha) {
		return (1 - alpha) * x1 + alpha * x2;
	}
	
	public static Box intersection(Sphere s, Line l) {
		Vector oc = l.o.minus(s.c);	
		double loc = l.l.dot(oc);
		double ls = l.l.lengthSquared();
		double x = loc*loc - ls * (oc.lengthSquared() - s.r*s.r);
		if ( x > 0 ) {
			double d2 = Math.sqrt(x);
			Vector lowerLeft = l.o.plus(l.l.times((-loc + d2)/ls));
			Vector topRight = l.o.plus(l.l.times((-loc - d2)/ls));
			return new Box(lowerLeft, topRight);
		} else {
			return null;
		}
	}

  public static double squared(double d) {
    return d*d;
  }
		
}
