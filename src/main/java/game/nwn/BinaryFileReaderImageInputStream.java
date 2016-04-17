package game.nwn;

import game.nwn.readers.BinaryFileReader;

import java.io.IOException;
import java.nio.ByteOrder;

import javax.imageio.stream.IIOByteBuffer;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.ImageInputStreamImpl;

public class BinaryFileReaderImageInputStream extends ImageInputStreamImpl {
	
	private BinaryFileReader inp;

	public BinaryFileReaderImageInputStream(BinaryFileReader inp) {
		this.inp = inp;
	}

	@Override
	public int read() throws IOException {
		return (int) inp.readByte();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException {
		byte[] bytes = inp.readBytes(len);
		for(int i=0;i<len;++i) {
			b[off + i] = bytes[i];
		}
		return len;
	}


}
