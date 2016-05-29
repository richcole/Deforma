package game.math;

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

	public static FloatBuffer toFloatBuffer(double[] values) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(values.length);
		for (int i = 0; i < values.length; ++i) {
			buf.put((float) values[i]);
		}
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
		for (Integer value : ints) {
			buf.put(value);
		}
		buf.flip();
		return buf;
	}

	public static FloatBuffer toFloatBuffer(List<Float> ints) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(ints.size());
		for (Float value : ints) {
			buf.put(value);
		}
		buf.flip();
		return buf;
	}

	public static ByteBuffer toByteBuffer(byte[] bytes) {
		ByteBuffer buf = BufferUtils.createByteBuffer(bytes.length).put(bytes);
		buf.flip();
		return buf;
	}

	public static ByteBuffer toByteBuffer(String name) {
		byte[] bytes = name.getBytes();
		ByteBuffer buf = BufferUtils.createByteBuffer(bytes.length + 1);
		buf.put(bytes);
		buf.put((byte) 0);
		buf.flip();
		return buf;
	}

	public static List<Integer> range(int lower, int upper) {
		List<Integer> result = Lists.newArrayList();
		for (int i = lower; i < upper; ++i) {
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

	public static double squared(double d) {
		return d * d;
	}

	public static double[] toDoubleArray3(List<Vector> vs) {
		double[] result = new double[vs.size() * 3];
		int idx = 0;
		for (Vector v : vs) {
			result[idx++] = v.x();
			result[idx++] = v.y();
			result[idx++] = v.z();
		}
		return result;
	}

	public static double[] toDoubleArray16(List<Matrix> vs) {
		double[] result = new double[vs.size() * 16];
		int idx = 0;
		for (Matrix v : vs) {
			for(int i=0;i<v.v.length;++i) {
				result[idx++] = v.v[i];
			}
		}
		return result;
	}

	public static double[] toDoubleArray16(Matrix ... vs) {
		double[] result = new double[vs.length * 16];
		int idx = 0;
		for (Matrix v : vs) {
			for(int i=0;i<v.v.length;++i) {
				result[idx++] = v.v[i];
			}
		}
		return result;
	}

	public static int[] toIntArray(List<Integer> is) {
		int[] result = new int[is.size()];
		int idx = 0;
		for(Integer i: is) {
			result[idx++] = i;
		}
		return result;
	}

	public static int[] toIntArray(Integer ... is) {
		int[] result = new int[is.length];
		int idx = 0;
		for(Integer i: is) {
			result[idx++] = i;
		}
		return result;
	}

	public static double[] toDoubleArray2(List<Vector> vs) {
		double[] result = new double[vs.size() * 2];
		int idx = 0;
		for (Vector v : vs) {
			result[idx++] = v.x();
			result[idx++] = v.y();
		}
		return result;
	}

	public static double[] toDoubleArray(List<Integer> vs) {
		double[] result = new double[vs.size()];
		int idx = 0;
		for (Integer v : vs) {
			result[idx++] = v;
		}
		return result;
	}

	public static double[] toDoubleArray(double ... vs) {
		double[] result = new double[vs.length];
		int idx = 0;
		for (double v : vs) {
			result[idx++] = v;
		}
		return result;
	}

	public static Quaternion lerp(double alpha, Quaternion p, Quaternion q) {
		return p.times(alpha).plus(q.times(1 - alpha));
	}

	public static Vector lerp(double alpha, Vector p, Vector q) {
		return p.times(alpha).plus(q.times(1 - alpha));
	}
}