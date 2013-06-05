package game.nwn.readers;

import game.Context;
import game.base.Image;
import game.imageio.TgaLoader;
import game.nwn.readers.BifReader.EntryHeader;

import java.io.File;
import java.util.Collection;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.common.base.Suppliers;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

public class KeyReader {
  
  private static Logger logger = Logger.getLogger(KeyReader.class);
  
  Context context;
  Map<Integer, BifReader> bifReaders = Maps.newHashMap();
  BinaryFileReader inp;
  Header header;
  Multimap<String, Resource> keyIndex;
  Map<String, Image> imageMap = Maps.newHashMap();

  public KeyReader(Context context, String keyFileName) {
    this.context = context;
    this.inp = new BinaryFileReader(new File(context.getNwnRoot(), keyFileName));
    this.header = readHeader();
    readKeyIndex();
  }
  
  private void readKeyIndex() {
    keyIndex = Multimaps.newListMultimap(Maps.<String, Collection<Resource>>newHashMap(), new ListSupplier<Resource>());
    for(int i=0;i<header.numKeys;++i) {
      KeyReader.KeyEntry entry = readKeyEntry(i);
      if ( entry.name.contains("c_wererat") ) {
        logger.info("model " + entry.name + " type " + ResourceType.getType(entry.type));
      }
      keyIndex.put(entry.name, createResource(entry));
    }
  }

  public BifReader getBifReader(int i) {
    BifReader bifReader = bifReaders.get(i);
    if ( bifReader == null ) {
      KeyReader.BifEntry entry = readBifEntry(i);
      File bifFile = new File(context.getNwnRoot(), entry.name);
      bifReader = new BifReader(bifFile);
      bifReaders.put(i, bifReader);
    }
    return bifReader;
  }
  
  public Image getImage(String name) {
    Image image = imageMap.get(name);
    if ( image == null ) {
      Resource r = getResource(name, ResourceType.TGA);
      TgaLoader imageLoader = new TgaLoader();
      image = imageLoader.readImage(r.reader.inp, r.offset, r.length);
      imageMap.put(name, image);
    }
    return image;
  }
  
  public MdlReader getMdlReader(String name) {
    MdlReader mdlReader = new MdlReader(this, getResource(name, ResourceType.MDL));
    return mdlReader;
  }
  
  private Resource getResource(String name, ResourceType type) {
    for(Resource r: keyIndex.get(name)) {
      if (r.entry.type == type.getId()) {
        return r;
      }
    }
    throw new RuntimeException("Unable to find resource with name " + name + " and type " + type);
  }
  
  private Resource createResource(KeyReader.KeyEntry entry) {
    int bifIndex = entry.getBifIndex();
    int resourceIndex = entry.getResourceIndex();
    BifReader bifReader = getBifReader(bifIndex);
    EntryHeader entryHeader = bifReader.readEntryHeader(resourceIndex); 
    return new Resource(bifReader, entryHeader.offset, (int)entryHeader.size, entry);
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
    int  nameSize;
    int  drive;
    String name;
  }

  static public class KeyEntry {
    String name;
    int    type;
    long   ids;
    
    int getBifIndex() {
      return (int)(ids >> 20);
    }
    
    int getResourceIndex() {
      return (int)(ids & 0xfffff);
    }
  }
  
  public Header readHeader() {
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
  
  public BifEntry readBifEntry(int i) {
    inp.seek(header.fileTableOffset + i*12);
    BifEntry fileEntry = new BifEntry();
    fileEntry.fileSize = inp.readWord();
    fileEntry.nameOffset = inp.readWord();
    fileEntry.nameSize = inp.readShort();
    fileEntry.drive = inp.readShort();
    fileEntry.name = inp.readStringAt(fileEntry.nameOffset, fileEntry.nameSize-1).replace("\\", "/");
    return fileEntry;
  }
  
  public KeyEntry readKeyEntry(int i) {
    inp.seek(header.keyTableOffset + i*22);
    KeyEntry fileEntry = new KeyEntry();
    fileEntry.name = inp.readNullString(16);
    fileEntry.type = inp.readShort();
    fileEntry.ids = inp.readWord();
    return fileEntry;
  }

}
