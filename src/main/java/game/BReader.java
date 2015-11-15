package game;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import com.google.common.base.Throwables;

public class BReader {
	
	FileInputStream is;
	
	public BReader(File file) {
		try {
			is = new FileInputStream(file);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
	}
	
	public BReader(FileInputStream is) {
		this.is = is;
	}
	
	int readu8() {
		int result = 0;
		try {
			result = is.read();
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		if ( result == -1 ) {
			throw new RuntimeException("EOF");
		}
		return result;
	}

	long readu32() {
		long result = 0;
		result = result | (readu8() << 0);
		result = result | (readu8() << 8);
		result = result | (readu8() << 16);
		result = result | (readu8() << 24);
		return result;
	}
	
	long[] readu32s(int numWords) {
		long[] words = new long[numWords];
		for(int i=0;i<words.length;++i) {
			words[i] = readu32();
		}
		return words;
	}
	
	int readu16() {
		int result = 0;
		result = result | (readu8() << 0);
		result = result | (readu8() << 8);
		return result;
	}
	
	byte[] readu8s(int numBytes) {
		byte[] bytes = new byte[numBytes];
		for(int i=0;i<bytes.length;++i) {
			bytes[i] = (byte)readu8();
		}
		return bytes;
	}

	public int available() {
		try {
			return is.available();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public String readZString() {
		StringBuffer buf = new StringBuffer();
		int c = readu8(); 
		while(c != 0) {
			buf.appendCodePoint(c);
			c = readu8();
		}
		return buf.toString();
	}

	public void skip(int n) {
		try {
			is.skip(n);
		} catch(IOException e) {
			Throwables.propagate(e);
		}
	}

	public long pos() {
		try {
			return is.getChannel().position();
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return 0;
	}

	public float readFloat() {
		ByteBuffer buf = ByteBuffer.allocate(4);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(readu8s(4));
		buf.flip();
		return buf.getFloat();
	}

	public double readDouble() {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(readu8s(8));
		buf.flip();
		return buf.getDouble();
	}

	public String readXPSString() {
		int len = (int) readXPSNibble();
		return new String(readu8s(len));
	}

	private long readXPSNibble() {
		long i = 0;
		long c = 0;
		while(i < 4) {
			long x = readu8();
			c |= (x << i*7);
			if ( (x & 0x80) == 0 ) {
				break;
			}
		}
		return c;
	}

	public float[] readFloats(int n) {
		float[] result = new float[n];
		for(int i=0;i<n;++i) {
			result[i] = readFloat();
		}
		return result;
	}

	public int[] readu16s(int n) {
		int[] result = new int[n];
		for(int i=0;i<n;++i) {
			result[i] = readu16();
		}
		return result;
	}
}
