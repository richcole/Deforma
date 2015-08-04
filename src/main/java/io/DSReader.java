package io;

import java.io.File;
import java.io.PrintStream;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import art.ImageTexture;
import art.Material;
import art.Mesh;
import game.ImageResource;
import game.math.Matrix;
import game.math.Vector;
import game.voxel.MeshGeom;

public class DSReader {
	
	private static final Logger log = LoggerFactory.getLogger(DSReader.class);
	
	BReader reader;
	
	DSReader(File file) {
		reader = new BReader(file);
	}
	
	public class Chunk {
		int id;
		long length;
		
		public Chunk() {
			super();
		}
		
		Chunk withId(int id) {
			this.id = id;
			return this;
		}

		
		Chunk withLength(long length) {
			this.length = length;
			return this;
		}
		
		Chunk parse(BReader reader, String indent) {
			reader.skip((int)(length - 6));
			return this;
		}


		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d\n", id, length);
		}

		public Chunk firstNode(int id) {
			if ( this.id == id ) {
				return this;
			}
			return null;
		}
		
		List<Chunk> eachNode(int id, List<Chunk> result) {
			if ( this.id == id ) {
				result.add(this);
			}
			return result;
		}
		
		List<Chunk> eachNode(int id) {
			return eachNode(id, Lists.<Chunk>newArrayList());
		}
	}
	
	public class ContainersChunk extends Chunk {
		List<Chunk> children = Lists.newArrayList();
		
		public ContainersChunk() {
			super();
		}
		
		Chunk parse(BReader reader, String indent) {
			int offset = 6;
			while(offset < length) {
				Chunk child = readChunk(indent + "  ");
				children.add(child);
				offset += child.length;
			}
			return this;
		}
		
		public void print(PrintStream out, int indent) {
			super.print(out, indent);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}
		
		public Chunk firstNode(int id) {
			if ( this.id == id ) {
				return this;
			}
			for(Chunk child: children) {
				Chunk cand = child.firstNode(id);
				if ( cand != null ) {
					return cand;
				}
			}
			return null;
		}
		
		List<Chunk> eachNode(int id, List<Chunk> result) {
			if ( this.id == id ) {
				result.add(this);
			}
			for(Chunk child: children) {
				child.eachNode(id, result);
			}
			return result;
		}

	}
	
	public class EditObjectChunk extends ContainersChunk {
		String str;
		
		public EditObjectChunk() {
			super();
		}

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			str = reader.readZString();
			offset += str.length() + 1;

			while(offset < length) {
				Chunk child = readChunk(indent + "  ");
				children.add(child);
				offset += child.length;
			}
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d %s\n", id, length, str);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}

	}
				
	public class VersionChunk extends ContainersChunk {
		long version;
		
		public VersionChunk() {
			super();
		}

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			version = reader.readWord();
			
			offset += 4;

			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d VersionChunk %d\n", id, length, version);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}

	}
				
	public class TranslationMatrixChunk extends Chunk {
		Matrix matrix = new Matrix(Matrix.IDENTITY);
		
		public TranslationMatrixChunk() {
			super();
		}

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			for(int i=0;i<4;++i) {
				for(int j=0;j<3;++j) {
					matrix.set(i, j, reader.readFloat());
				}
			}
			
			offset += 4 * 12;

			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d TranslationMatrixChunk %d\n", id, length, matrix);
		}

	}

	public class FloatChunk extends Chunk {
		float value = 0.0f;
		
		public FloatChunk() {
			super();
		}

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			value = reader.readFloat();
			
			offset += 4;

			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d FloatChunk %d\n", id, length, value);
		}

	}

	public class WordChunk extends Chunk {
		long value = 0L;
		
		public WordChunk() {
			super();
		}

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			value = reader.readWord();
			
			offset += 4;
			
			reader.readBytes((int)(length - offset));

			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d WordChunk %d\n", id, length, value);
		}

	}

	public class ShortChunk extends Chunk {
		int value = 0;
		
		public ShortChunk() {
			super();
		}

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			value = reader.readShort();
			
			offset += 2;
			
			reader.readBytes((int)(length - offset));

			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d ShortChunk %d\n", id, length, value);
		}

	}

	public class FaceListChunk extends ContainersChunk {
		int n;
		int indexes[];
		int info[];

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			n = reader.readShort();
			indexes = new int[n*3];
			info = new int[n];
			for(int i=0;i<n;++i) {
				for(int j=0;j<3;++j) {
					indexes[i*3+j] = reader.readShort();
				}
				info[i] = reader.readShort();
			}

			offset += 2;
			offset += n * 4 * 2; 
			
			while(offset < length) {
				Chunk child = readChunk(indent + "  ");
				children.add(child);
				offset += child.length;
			}
				
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d FaceListChunk %d\n", id, length, n);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}

	}

	public class TexCoordsChunk extends ContainersChunk {
		int n;
		float uv[];

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			n = reader.readShort();
			uv = new float[n*3];
			for(int i=0;i<n;++i) {
				for(int j=0;j<2;++j) {
					uv[i*2+j] = reader.readFloat();
				}
			}

			offset += 2;
			offset += n * 2 * 4; 
			
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d FaceListChunk %d\n", id, length, n);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}

	}

	public class FaceMaterialChunk extends Chunk {
		String materialName;
		int n;
		int faces[];

		Chunk parse(BReader reader, String indent) {
			int offset = 6;
			
			materialName = reader.readZString();
			n = reader.readShort();
			faces = new int[n];
			for(int i=0;i<n;++i) {
				faces[i] = reader.readShort();
			}

			offset += materialName.length() + 1;
			offset += 2;
			offset += n * 2; 
			
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d FaceMaterial %s %d\n", id, length, materialName, n);
		}

	}

	public class MaterialNameChunk extends Chunk {
		String materialName;

		Chunk parse(BReader reader, String indent) {
			int offset = 6;
			
			materialName = reader.readZString();

			offset += materialName.length() + 1;
			
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d FaceMaterial %s %d\n", id, length, materialName);
		}

	}

	public class MappingFilenameChunk extends Chunk {
		String textureFilename;

		Chunk parse(BReader reader, String indent) {
			int offset = 6;
			
			textureFilename = reader.readZString();

			offset += textureFilename.length() + 1;
			
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d TextureFilenameChunk %s %d\n", id, length, textureFilename);
		}

	}

	public class FramesChunk extends ContainersChunk {
		long start;
		long end;

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			start = reader.readWord();
			end = reader.readWord();

			offset += 8;
			
			while(offset < length) {
				Chunk child = readChunk(indent + "  ");
				children.add(child);
				offset += child.length;
			}
				
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d FramesChunk %d %d\n", id, length, start, end);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}

	}

	public class VertexListChunk extends ContainersChunk {
		public int n;
		public double vs[];

		Chunk parse(BReader reader, String indent) {
			int offset = 6;

			n = reader.readShort();
			vs = new double[n*3];
			for(int i=0;i<n;++i) {
				for(int j=0;j<3;++j) {
					vs[i*3+j] = reader.readFloat();
				}
			}

			offset += 2;
			offset += n * 3 * 4; 
			
			while(offset < length) {
				Chunk child = readChunk(indent + "  ");
				children.add(child);
				offset += child.length;
			}
				
			return this;
		}

		public void print(PrintStream out, int indent) {
			for(int i=0;i<indent;++i) {
				out.print(" ");
			}
			out.format("%04x %d VertexListChunk %d\n", id, length, n);
			for(Chunk child: children) {
				child.print(out, indent + 2);
			}
		}

	}

	public Chunk newChunk(int id, long length) {
		try {
			Chunk chunk;
			switch(id) {
			case 0x4D4D:
			case 0x3D3D:
			case 0xAFFF:
			case 0x7012:
			case 0x4100:
			case 0xb000:
				chunk = new ContainersChunk();
				break;
				
			case 0xb008:
				chunk = new FramesChunk();
				break;
				
			case 0x4000:
				chunk = new EditObjectChunk();
				break;
			case 0x4120:
				chunk = new FaceListChunk();
				break;
			case 0x4110:
				chunk = new VertexListChunk();
				break;
			case 0x0002:
				chunk = new VersionChunk();
				break;
			case 0x4140:
				chunk = new TexCoordsChunk();
				break;
			case 0x4130:
				chunk = new FaceMaterialChunk();
				break;
				
			case 0xa000:
				chunk = new MaterialNameChunk();
				break;
				
			case 0xa200:
				chunk = new ContainersChunk();
				break;

			case 0xa300:
				chunk = new MappingFilenameChunk();
				break;

			case 0xa354:
			case 0xa356:
			case 0xa358:
			case 0xa35a:
				chunk = new FloatChunk();
				break;
				
			case 0xa351:
				chunk = new ShortChunk();
				break;
				
			case 0x0030:
			case 0x0144:
				
			case 0x0100:
			case 0x2100:

			case 0xa010:
			case 0xa020:
			case 0xa030:
			case 0xa040:
			case 0xa041:
			case 0xa050:
			case 0xa051:
			case 0xa052:
			case 0xa053:
			case 0xa100:
			case 0xa084:
			case 0xa081:
			case 0xa087:
			case 0xa210:

			case 0xb001:
			case 0xb002:
			case 0xb003:
			case 0xb004:
			case 0xb005:
			case 0xb006:
			case 0xb007:
			case 0xb009:
			case 0xb00a:
			case 0xb010:
			case 0xb013:
			case 0xb015:
			case 0xb020:
			case 0xb021:
			case 0xb022:
			case 0xb023:
			case 0xb024:
			case 0xb025:
			case 0xb026:
			case 0xb027:
			case 0xb028:
			case 0xb029:
			case 0xb030:
				
			case 0x3d3e:
			case 0xa353:
				      
			case 0x4160:
			case 0x4150:
				chunk = new Chunk();
				break;
			default:
				throw new RuntimeException(String.format("No chunk for id %04x", id));
			}
			chunk.withId(id);
			chunk.withLength(length);
			return chunk;
		} catch (Exception e) {
			Throwables.propagate(e);
		}
		return null;
	}

	public Chunk readChunk(String indent) {
		long pos = reader.pos();
		int id = reader.readShort();
		long length = reader.readWord();
		
		log.debug(String.format("%s id %04x length %d pos %x", indent, id, length, pos));
		
		Chunk chunk = newChunk(id, length);
		chunk.parse(reader, indent);
				
		return chunk;
	}
	
	public List<Mesh> getMeshGeom(File baseDir, Chunk root, Material mat) {
		List<Mesh> meshList = Lists.newArrayList();
		
		for(Chunk parent: root.eachNode(0x4100)) {

			Map<String, Material> materials = Maps.newHashMap();
			for(Chunk chunk: root.eachNode(0xafff)) {
				DSReader.MappingFilenameChunk fChunk = (DSReader.MappingFilenameChunk) chunk.firstNode(0xa300);
				DSReader.MaterialNameChunk nChunk = (DSReader.MaterialNameChunk) chunk.firstNode(0xa000);
				DSReader.FloatChunk vScale = (DSReader.FloatChunk) chunk.firstNode(0xa354);
				DSReader.FloatChunk uScale = (DSReader.FloatChunk) chunk.firstNode(0xa356);
				DSReader.FloatChunk uOffset = (DSReader.FloatChunk) chunk.firstNode(0xa358);
				DSReader.FloatChunk vOffset = (DSReader.FloatChunk) chunk.firstNode(0xa35a);
				DSReader.ShortChunk tilingParams = (DSReader.ShortChunk) chunk.firstNode(0xa351);
				if ( fChunk != null && nChunk != null ) {
					log.debug(fChunk.textureFilename + " " + nChunk.materialName);
					if ( uScale != null ) {
						log.debug("uScale " + uScale.value);
					}
					if ( vScale != null ) {
						log.debug("vScale " + vScale.value);
					}
					if ( uOffset != null ) {
						log.debug("uOffset " + uOffset.value);
					}
					if ( vOffset != null ) {
						log.debug("vOffset " + vOffset.value);
					}
					if ( tilingParams != null ) {
						log.debug("tiling " + tilingParams.value);
					}
					File imageFile = findImageFile(baseDir, fChunk.textureFilename);
					Preconditions.checkNotNull(imageFile);
					ImageResource img = new ImageResource(imageFile);
					materials.put(nChunk.materialName, new ImageTexture(img));
				}
			}
	
			Set<Integer> existingFaces = Sets.newHashSet();
			VertexListChunk vs = (VertexListChunk) parent.firstNode(0x4110);
			TexCoordsChunk texCoords = (TexCoordsChunk) parent.firstNode(0x4140);
			for(Chunk chunk: parent.eachNode(0x4120)) {
				FaceListChunk faces = (FaceListChunk) chunk;
				
				for(Chunk fChunk: parent.eachNode(0x4130)) {
					
					FaceMaterialChunk faceMaterials = (FaceMaterialChunk) fChunk;
		
					Material material = materials.get(faceMaterials.materialName);
					
					if ( material == null ) {
						log.debug("Using default material");
						material = mat;
					}
					Mesh mesh = new Mesh(material);
					
					MeshGeom geom = new MeshGeom();
					for(int i=0;i<vs.vs.length/3;++i) {
						Vector p = new Vector(vs.vs[i*3], vs.vs[i*3+1], vs.vs[i*3+2], 1.0);
						float u = texCoords.uv[i*2];
						float v = texCoords.uv[i*2+1];
						Vector t = new Vector(u, 1.0 - v, 0, 1.0);
						geom.addVertex(p, Vector.Z, t);
					}
					for(int faceIndex=0;faceIndex<faceMaterials.n;++faceIndex) {
						int index = faceMaterials.faces[faceIndex];
						existingFaces.add(index);
						geom.addElement(faces.indexes[index*3], faces.indexes[index*3+1], faces.indexes[index*3+2]);
					}
					
					log.debug("Adding mesh " + faceMaterials.n + " " + faceMaterials.materialName);
					
					mesh.addGeom(geom);
					meshList.add(mesh);
				}
			
			}
		}
		
		return meshList;
	}

	private File findImageFile(File baseDir, String fileName) {
		for(File child: baseDir.listFiles()) {
			if (isNameCloseEnough(fileName, child)) {
				return child;
			}
			if (child.isDirectory()) {
				File cand = findImageFile(child, fileName);
				if ( cand != null ) {
					return cand;
				}
			}
		}
		return null;
	}

	private boolean isNameCloseEnough(String fileName, File child) {
		return child.getName().equals(fileName) || child.getName().startsWith(fileName);
	}
}
