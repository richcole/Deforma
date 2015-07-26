package io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

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
	
	int readByte() {
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

	long readWord() {
		long result = 0;
		result = result | (readByte() << 0);
		result = result | (readByte() << 8);
		result = result | (readByte() << 16);
		result = result | (readByte() << 24);
		return result;
	}
	
	long[] readWords(int numWords) {
		long[] words = new long[numWords];
		for(int i=0;i<words.length;++i) {
			words[i] = readWord();
		}
		return words;
	}
	
	int readShort() {
		int result = 0;
		result = result | (readByte() << 0);
		result = result | (readByte() << 8);
		return result;
	}
	
	byte[] readBytes(int numBytes) {
		byte[] bytes = new byte[numBytes];
		for(int i=0;i<bytes.length;++i) {
			bytes[i] = (byte)readByte();
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
		int c = readByte(); 
		while(c != 0) {
			buf.appendCodePoint(c);
			c = readByte();
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
		buf.put(readBytes(4));
		buf.flip();
		return buf.getFloat();
	}

	public double readDouble() {
		ByteBuffer buf = ByteBuffer.allocate(8);
		buf.order(ByteOrder.LITTLE_ENDIAN);
		buf.put(readBytes(8));
		buf.flip();
		return buf.getDouble();
	}
}
