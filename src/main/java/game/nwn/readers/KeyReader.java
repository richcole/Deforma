package game.nwn.readers;

import game.image.Image;
import game.nwn.readers.BifReader.EntryHeader;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class KeyReader {

	private static Logger log = LoggerFactory.getLogger(KeyReader.class);

	private Map<String, Map<Integer, BifReader>> bifReaders = Maps.newHashMap();
	private Multimap<String, Resource> keyIndex = Multimaps
			.newListMultimap(Maps.<String, Collection<Resource>> newHashMap(), new ListSupplier<Resource>());
	private Map<String, Image> imageMap = Maps.newHashMap();
	private Map<String, MdlModel> modelMap = Maps.newHashMap();

	public KeyReader(File nwnRoot) {
		Stack<File> stack = new Stack<File>();
		stack.push(nwnRoot);
		try {
			while(! stack.isEmpty() ) {
				File root = stack.pop();
				log.info("Searching " + root.getAbsolutePath());
				if ( root.exists() ) {
					for (File keyFile : root.listFiles()) {
						String keyFileName = keyFile.getName();
						if (keyFileName.endsWith(".key")) {
							log.info("Loading " + keyFile.getAbsolutePath());
							Map<Integer, BifReader> emptyMap = Maps.newHashMap();
							bifReaders.put(keyFileName, emptyMap);
							BinaryFileReader inp = new BinaryFileReader(keyFile);
							Header header = readHeader(inp);
							readKeyIndex(root, keyFileName, header, inp);
						}
						if ( keyFile.isDirectory() ) {
							stack.push(keyFile);
						}
					}
				}
			}
		} catch (Exception e) {
			Throwables.propagate(e);
		}
	}

	private void readKeyIndex(File root, String keyName, Header header, BinaryFileReader inp) {
		for (int i = 0; i < header.numKeys; ++i) {
			KeyReader.KeyEntry entry = readKeyEntry(header, inp, i);			
			keyIndex.put(entry.getName(), createResource(root, keyName, header, inp, entry));
		}
	}

	public BifReader getBifReader(File root, String keyName, Header header, BinaryFileReader inp, int i) {
		Map<Integer, BifReader> bifReadersForKey = bifReaders.get(keyName);
		BifReader bifReader = bifReadersForKey.get(i);
		if (bifReader == null) {
			KeyReader.BifEntry entry = readBifEntry(header, inp, i);
			File bifFile = new File(root, entry.name);
			bifReader = new BifReader(entry, bifFile);
			bifReadersForKey.put(i, bifReader);
		}
		return bifReader;
	}

	public List<Image> getImageList(String name) {
		List<Image> imageList = Lists.newArrayList();
		List<Resource> rs = getResourceList(name, ResourceType.TGA);
		for(Resource r: rs) {
			BinaryFileReader inp = r.getReader().getInp();
			inp.seek(r.getOffset());
			try {
				imageList.add(new TgaLoader().readImage(inp));
			} catch (IOException e) {
				throw new RuntimeException("Unable to read " + name, e);
			}
		}
		return imageList;
	}
	
	public Image getImage(String name) {
		Image image = imageMap.get(name);
		if (image == null) {
			List<Resource> rs = getResourceList(name, ResourceType.TGA);
			for(Resource r: rs) {
				BinaryFileReader inp = r.getReader().getInp();
				inp.seek(r.getOffset());
				try {
					image = new TgaLoader().readImage(inp);
					imageMap.put(name, image);
				} catch (IOException e) {
					throw new RuntimeException("Unable to read " + name, e);
				}
			}
		}
		return image;
	}

	public MdlReader getMdlReader(String name) {
		MdlReader mdlReader = new MdlReader(this, getResource(name, ResourceType.MDL));
		return mdlReader;
	}
	
	public List<Resource> getResourceList(String name, ResourceType type) {
		Collection<Resource> resList = keyIndex.get(name.toLowerCase());
		List<Resource> result = Lists.newArrayList();
		for (Resource r : resList) {
			log.info(r.getName() + " " + r.getEntry().getType());
			if (r.getEntry().getType() == type.getId()) {
				 result.add(r);
			}
		}
		return result;
	}

	public List<Resource> getResourceListPrefix(String name, ResourceType type) {
		List<Resource> result = Lists.newArrayList();
		for (Resource r : keyIndex.values()) {
			if (r.getName().toLowerCase().startsWith(name.toLowerCase()) && r.getEntry().getType() == type.getId()) {
				log.info("File " + r.getName() + " type=" + r.getEntry().getType());
				result.add(r);
			}
		}
		return result;
	}

	public Resource getResource(String name, ResourceType type) {
		Collection<Resource> resList = keyIndex.get(name.toLowerCase());
		Resource matchingRes = null;
		for (Resource r : resList) {
			log.info(r.getName() + " " + r.getEntry().getType());
			if (r.getEntry().getType() == type.getId()) {
				 matchingRes = r;
			}
		}
		if ( matchingRes != null ) {
			return matchingRes;
		}
		for (Resource r : keyIndex.values()) {
			if (r.getName().equalsIgnoreCase(name)) {
				log.info("File " + r.getName() + " type=" + r.getEntry().getType());
			}
		}
		throw new RuntimeException("Unable to find resource with name " + name + " and type " + type);
	}

	private Resource createResource(File root, String keyName, Header header, BinaryFileReader inp, KeyReader.KeyEntry entry) {
		int bifIndex = entry.getBifIndex();
		int resourceIndex = entry.getResourceIndex();
		BifReader bifReader = getBifReader(root, keyName, header, inp, bifIndex);
		EntryHeader entryHeader = bifReader.readEntryHeader(resourceIndex);
		return new Resource(bifReader, entryHeader.offset, (int) entryHeader.size, entry);
	}

	static public class Header {
		String type;
		String version;
		long numBif;
		long numKeys;
		long fileTableOffset;
		long keyTableOffset;
		long buildYear;
		long buildDay;
		byte[] reserved;
	}

	static public class BifEntry {
		long fileSize;
		long nameOffset;
		int nameSize;
		int drive;
		String name;
	}

	static public class KeyEntry {
		private String name;
		private int type;
		long ids;

		int getBifIndex() {
			return (int) (ids >> 20);
		}

		int getResourceIndex() {
			return (int) (ids & 0xfffff);
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getType() {
			return type;
		}

		public void setType(int type) {
			this.type = type;
		}
	}

	public Header readHeader(BinaryFileReader inp) {
		Header header = new Header();
		header.type = inp.readString(4);
		header.version = inp.readString(4);
		header.numBif = inp.readWord();
		header.numKeys = inp.readWord();
		header.fileTableOffset = inp.readWord();
		header.keyTableOffset = inp.readWord();
		header.buildYear = inp.readWord();
		header.buildDay = inp.readWord();
		header.reserved = inp.readBytes(32);
		return header;
	}

	public BifEntry readBifEntry(Header header, BinaryFileReader inp, int i) {
		inp.seek(header.fileTableOffset + i * 12);
		BifEntry fileEntry = new BifEntry();
		fileEntry.fileSize = inp.readWord();
		fileEntry.nameOffset = inp.readWord();
		fileEntry.nameSize = inp.readShort();
		fileEntry.drive = inp.readShort();
		fileEntry.name = inp.readStringAt(fileEntry.nameOffset, fileEntry.nameSize - 1).replace("\\", "/");
		return fileEntry;
	}

	public KeyEntry readKeyEntry(Header header, BinaryFileReader inp, int i) {
		inp.seek(header.keyTableOffset + i * 22);
		KeyEntry fileEntry = new KeyEntry();
		fileEntry.setName(inp.readNullString(16));
		fileEntry.setType(inp.readShort());
		fileEntry.ids = inp.readWord();
		return fileEntry;
	}

	public MdlModel getModel(String modelName) {
		MdlModel model = modelMap.get(modelName);
		if (model == null) {
			MdlReader modelReader = new MdlReader(this, getResource(modelName, ResourceType.MDL));
			model = modelReader.readModel();
			modelMap.put(modelName, model);
		}
		return model;
	}

	public Multimap<String, Resource> getKeyIndex() {
		return keyIndex;
	}

}
