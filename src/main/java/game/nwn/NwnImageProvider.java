package game.nwn;

import game.image.BufferedImageImage;
import game.image.Image;
import game.image.RawImage;
import game.nwn.readers.BinaryFileReader;
import game.nwn.readers.ErfReaderList;
import game.nwn.readers.KeyReader;
import game.nwn.readers.Resource;
import game.nwn.readers.ResourceType;
import game.nwn.readers.TgaLoader;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.MemoryCacheImageInputStream;

import com.google.common.base.Throwables;

public class NwnImageProvider implements Function<String, Image> {
	
	private KeyReader keyReader;
	private ErfReaderList erfReaderList;
	
	public NwnImageProvider(ErfReaderList erfReaderList, KeyReader reader) {
		this.erfReaderList = erfReaderList;
		this.keyReader = reader;
	}

	@Override
	public Image apply(String name) {
		byte[] ddsBytes = erfReaderList.getResource(name, ResourceType.DDS);

		if ( ddsBytes != null ) {
			if (ddsBytes[0] == 'D' && ddsBytes[1] == 'D' && ddsBytes[2] == 'S' && ddsBytes[3] == ' ') {
				Iterator<ImageReader> iterator = ImageIO.getImageReadersBySuffix("dds");
				while (iterator.hasNext()) {
					try {
						ImageReader imageReader = iterator.next();
						imageReader.setInput(new MemoryCacheImageInputStream(new ByteArrayInputStream(ddsBytes)));
						int numImages = imageReader.getNumImages(true);
						if ( numImages > 0 ) {
							return new BufferedImageImage(imageReader.read(0));
						}
					}
					catch(Exception e) {
						Throwables.propagate(e);
					}
				}
				throw new RuntimeException("Unable to load DDS file " + name);
			}
			else {
				try {
					ByteBuffer buf = ByteBuffer.wrap(ddsBytes);
					buf.order(ByteOrder.LITTLE_ENDIAN);
					int width = buf.getInt();
					int height = buf.getInt();
					int bpp = buf.getInt();
					
					int offset = 12;
					byte[] outputBytes = new byte[width*height*4];
					for(int i=3; i<outputBytes.length; i+=4) {
						outputBytes[i] = (byte) 0xff;
					}
					
					if ( bpp == 3) {
						int bh = height / 4;
						int bw = width / 4;
						for(int by = 0; by < bh; ++by) {
							for(int bx = 0; bx < bw; ++bx) {
								decodeColorBlockDXT1(ddsBytes, offset, bx, by, bw, 8, outputBytes, false);
							}
						}
					}
					else if ( bpp == 4 ) {
						int bh = height / 4;
						int bw = width / 4;
						for(int by = 0; by < bh; ++by) {
							for(int bx = 0; bx < bw; ++bx) {
								// decodeColorBlockDXT5(ddsBytes, offset, bx, by, bw, 16, outputBytes);
								decodeColorBlockDXT1(ddsBytes, offset, bx, by, bw, 16, outputBytes, false);
							}
						}
					}
					else {
						throw new RuntimeException("Unsupported format");
					}
					RawImage image = new RawImage(width, height, outputBytes);
					writeImage(name, image);
					return image;
				}
				catch(Exception e) {
					Throwables.propagate(e);
				}
				
				return null;
			}
		}
		else {
			return loadTGA(name);			
		}
	}
	
	private void decodeColorBlockDXT5(byte[] bytes, int offset, int bx, int by, int bs, int bw, byte[] outputBytes) throws IOException{
		
		offset += ((by * bw) + bx) * bs;

		int color0, color1, bits0, bits1, bits2, bits3, bits4, bits5;
		int[] color = new int[8];
		int[] bits = new int[6];

		//Read 8 Bytes
		color0 = bytes[offset+0] & 0xff;
		color1 = bytes[offset+1] & 0xff;
		bits0 =  bytes[offset+2] & 0xff;
		bits1 =  bytes[offset+3] & 0xff;
		bits2 =  bytes[offset+4] & 0xff;
		bits3 =  bytes[offset+5] & 0xff;
		bits4 =  bytes[offset+6] & 0xff;
		bits5 =  bytes[offset+7] & 0xff;

		//64bit UNSIGNED LONG - broke up into 6 ints
		bits[0] = bits0 + 256 * (bits1 + 256);
		bits[1] = bits1 + 256 * (bits2 + 256);
		bits[2] = bits2 + 256 * (bits3 + 256);
		bits[3] = bits3 + 256 * (bits4 + 256);
		bits[4] = bits4 + 256 * (bits5);
		bits[5] = bits5;
		
		//FIXME is not calulated... should:  multiplying by 1/255.
		if (color0 > -128){
			color[0] = color0;
		} else {
			color[0] = - 1;
		}
		if (color1 > -128){
			color[1] = color1;
		} else {
			color[1] = - 1;
		}
		//alpha[0] = (int) (alpha0 * (1.0/255.0));
		//alpha[1] = (int) (alpha1 * (1.0/255.0));

		if (color0 > color1){
			color[2] = (6*color[0] + 1*color[1])/7;
            color[3] = (5*color[0] + 2*color[1])/7;
            color[4] = (4*color[0] + 3*color[1])/7;
            color[5] = (3*color[0] + 4*color[1])/7;
            color[6] = (2*color[0] + 5*color[1])/7;
            color[7] = (1*color[0] + 6*color[1])/7;
		} else {
			color[2] = (4*color[0] + 1*color[1])/5;
			color[3] = (3*color[0] + 2*color[1])/5;
			color[4] = (2*color[0] + 3*color[1])/5;
			color[5] = (1*color[0] + 4*color[1])/5;
			color[6] = 0;
			color[7] = 255;
		}
		int i, bit;
		byte code;
		for (int yi = 0; yi < 4 ; yi++){
			for (int xi = 0; xi < 4; xi++){
				//Calculate the bit posistion in the alpha0, alpha1 (long)
				
				i = (3*(4*yi+xi)); // 64bit UNSIGNED LONG: bit position
				
				bit = (int) Math.floor(i / 8.0);  //where in the bits array to find the bit position
				i = i - (bit * 8); //offset from the 64bit position to the bits array

				//Extract 2bits, from the bits array
				code = (byte)((bits[bit] >> i) & 7);
				
				//Add the value to the alpha map
				int oy = by * 4 + yi;
				int ox = bx * 4 + xi;
				int ow = bw * 4;
				int o  = (oy*ow+ox)*4;
				outputBytes[o+3] = (byte) color[code];
			}
		}
	}
	
	private void decodeColorBlockDXT1(byte[] bytes, int offset, int bx, int by, int bw, int bs, byte[] outputBytes, boolean hasAlpha) throws IOException{
		
		offset += ((by * bw) + bx) * bs;
		
		int c0lo, c0hi, c1lo, c1hi, bits0, bits1, bits2, bits3;
		//Read 8 bytes
		c0lo =  bytes[offset+0] & 0xff;
		c0hi =  bytes[offset+1] & 0xff;
		c1lo =  bytes[offset+2] & 0xff;
		c1hi =  bytes[offset+3] & 0xff;
		bits0 = bytes[offset+4] & 0xff;
		bits1 = bytes[offset+5] & 0xff;
		bits2 = bytes[offset+6] & 0xff;
		bits3 = bytes[offset+7] & 0xff;

		
		long bits = bits0 + 256 * (bits1 + 256 * (bits2 + 256 * bits3));
		int color0 = (c0lo + c0hi * 256);
		int color1 = (c1lo + c1hi * 256);
		
		int[][] colorsRGBA = new int[4][4];
		colorsRGBA[0] = unpackRBG565(color0);
		colorsRGBA[1] = unpackRBG565(color1);
		
		if (color0 > color1) {
			//RGB
			for (int i = 0; i < 3; i++){
				colorsRGBA[2][i] = (( 2 * colorsRGBA[0][i] + colorsRGBA[1][i]) / 3) ;
				colorsRGBA[3][i] = (( colorsRGBA[0][i] + 2 * colorsRGBA[1][i]) / 3);
			}
			//Transparency 
			colorsRGBA[2][3] = 255;
			colorsRGBA[3][3] = 255;
		} else { 
			//RGB
			for (int i = 0; i < 3; i++){
				colorsRGBA[2][i] = ((colorsRGBA[0][i] + colorsRGBA[1][i]) / 2);
				colorsRGBA[3][i] = 0; //Black or tranperant
			}
			//Transparency 
			colorsRGBA[2][3] = 255;
			if (hasAlpha){
				colorsRGBA[3][3] = 0;
			} else {
				colorsRGBA[3][3] = 255;
			}
		}
		int i = 0;
		for (int yi = 0; yi < 4 ; yi++){
			for (int xi = 0; xi < 4; xi++){
				byte code = (byte)((bits >> i*2) & 3);
				int oy = by * 4 + yi;
				int ox = bx * 4 + xi;
				int ow = bw * 4;
				int o  = (oy*ow+ox)*4;
				outputBytes[o+0] = (byte) colorsRGBA[code][0];
				outputBytes[o+1] = (byte) colorsRGBA[code][1];
				outputBytes[o+2] = (byte) colorsRGBA[code][2];
				if ( hasAlpha ) {
					outputBytes[o+3] = (byte) colorsRGBA[code][3];
				}
				i++;
			}
		}
	}	

	private int[] unpackRBG565(int rbg565){
		int r = (rbg565 >> 11) & 0x1f;
		int g = (rbg565 >>  5) & 0x3f;
		int b = (rbg565      ) & 0x1f;

		int[] color = new int[4];
		color[0] = (char)((r << 3) | (r >> 2));
		color[1] = (char)((g << 2) | (g >> 4));
		color[2] = (char)((b << 3) | (b >> 2));
		color[3] = 255;
		
		return color;
	}
	
	
	public Image loadTGA(String name) {
		List<Resource> resList = keyReader.getResourceList(name, ResourceType.TGA);
		Image bestImage = null;
		for(Resource res: resList) {
			BinaryFileReader inp = res.getReader().getInp();
			inp.seek(res.getOffset());
			try {
				Image image = new TgaLoader().readImage(inp);
				writeImage(name, image);
				if ( bestImage == null || (image.getWidth() > bestImage.getWidth()) ) {
					bestImage = image;
				}
			}
			catch(Exception e) {
				Throwables.propagate(e);
			}
		}
		return bestImage;
	}

	private void writeImage(String name, Image image) {
		BufferedImage img = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
		byte[] data = image.getRGBA();
		for(int i=0;i<image.getHeight(); ++i) {
			for(int j=0;j<image.getWidth(); ++j) {
				int r = data[(i*image.getWidth()+j)*4+0] & 0xff;
				int g = data[(i*image.getWidth()+j)*4+1] & 0xff;
				int b = data[(i*image.getWidth()+j)*4+2] & 0xff;
				int a = data[(i*image.getWidth()+j)*4+3] & 0xff;
				try {
					img.setRGB(j, i, (r << 16) | (g << 8) | b);
				}
				catch(Exception e) {
					throw new RuntimeException("Unable to write pixel", e);
				}
			}
		}
		try {
			ImageIO.write(img, "png", new File(name + ".png"));
		}
		catch(Exception e) {
			throw new RuntimeException("Unable to write file", e);
		}
	}

}
