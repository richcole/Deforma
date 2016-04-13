package game.nwn.readers;

import java.io.IOException;

import game.image.Image;
import game.image.RawImage;

public class TgaLoader {
	
	private static int btoi(byte[] buf, int index) {
		byte b = buf[index];
		int a = b;
		return (a < 0 ? 256 + a : a);
	}
	
	public Image readImage(BinaryFileReader inp) throws IOException {
		// Reading header bytes
		// buf[2]=image type code 0x02=uncompressed BGR or BGRA
		// buf[12]+[13]=width
		// buf[14]+[15]=height
		// buf[16]=image pixel size 0x20=32bit, 0x18=24bit
		// buf{17]=Image Descriptor Byte=0x28 (00101000)=32bit/origin
		// upperleft/non-interleaved
		
		byte[] buf = inp.readBytes(18);
		int width  = btoi(buf, 12) + (btoi(buf, 13) << 8); // 00,04=1024
		int height = btoi(buf, 14) + (btoi(buf, 15) << 8); // 40,02=576

		int n = width * height;
		byte[] pixels = new byte[n*4];
		int idx = 0;

		if (buf[2] == 0x02 && buf[16] == 0x20) { // uncompressed BGRA
			while (n > 0) {
				long b = inp.readByte();
				long g = inp.readByte();
				long r = inp.readByte();
				long a = inp.readByte();
				pixels[idx++] = (byte) r;
				pixels[idx++] = (byte) g;
				pixels[idx++] = (byte) b;
				pixels[idx++] = (byte) a;
				n -= 1;
			}
		} else if (buf[2] == 0x02 && buf[16] == 0x18) { // uncompressed BGR
			while (n > 0) {
				long b = inp.readByte();
				long g = inp.readByte();
				long r = inp.readByte();
				long a = 255; // opaque pixel
				pixels[idx++] = (byte) r;
				pixels[idx++] = (byte) g;
				pixels[idx++] = (byte) b;
				pixels[idx++] = (byte) a;
				n -= 1;
			}
		} else {
			// RLE compressed
			while (n > 0) {
				long nb = inp.readByte(); // num of pixels
				if ((nb & 0x80) == 0) { // 0x80=dec 128, bits 10000000
					for (int i = 0; i <= nb; i++) {
						long b = inp.readByte();
						long g = inp.readByte();
						long r = inp.readByte();
						long a = 0xff;
						pixels[idx++] = (byte) r;
						pixels[idx++] = (byte) g;
						pixels[idx++] = (byte) b;
						pixels[idx++] = (byte) a;
					}
				} else {
					nb &= 0x7f;
					long b = inp.readByte();
					long g = inp.readByte();
					long r = inp.readByte();
					long a = 0xff;
					for (int i = 0; i <= nb; i++) {
						pixels[idx++] = (byte) r;
						pixels[idx++] = (byte) g;
						pixels[idx++] = (byte) b;
						pixels[idx++] = (byte) a;
					}
				}
				n -= nb + 1;
			}
		}

		return new RawImage(width, height, pixels);
	}
}
