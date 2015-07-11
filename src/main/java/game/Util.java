package game;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;

import org.lwjgl.BufferUtils;

import com.google.common.collect.Lists;

public class Util {

	public static FloatBuffer toFloatBuffer(float[] floats) {
		FloatBuffer buf = BufferUtils.createFloatBuffer(floats.length).put(floats);
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

}
