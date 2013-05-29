package game.nwn;

import game.nwn.BifReader.EntryHeader;

import java.io.File;
import java.util.Map;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestReader {

  Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
  File root = new File("/mnt/nwn/");

  KeyReader keyReader;
  Map<Integer, BifReader> bifReaders = Maps.newHashMap();

  public void println(String string) {
    System.out.println(string);
  }
  
  public void printlnJson(Object obj) {
    System.out.println(gson.toJson(obj));
  }
  
  public static void main(String[] args) {
    new TestReader().run();
  }
  
  public void run() {
    keyReader = new KeyReader(new File(root, "chitin.key"));
    for(int i=0;i<keyReader.header.numBif;++i) {
      KeyReader.BifEntry entry = keyReader.readBifEntry(i);
      File bifFile = new File(root, entry.name);
      BifReader bifReader = new BifReader(bifFile);
      bifReaders.put(i, bifReader);
    }
    for(int i=0;i<keyReader.header.numKeys;++i) {
      KeyReader.KeyEntry entry = keyReader.readKeyEntry(i);
      if ( entry.type == 2002 ) {
        Resource resource = getResource(entry);
        writeEntry(resource, new File("/tmp/out"));
        MdlReader mdlReader = new MdlReader(resource);
        if ( mdlReader.header.zero == 0 && mdlReader.header.model.classification != 0 ) {
          printlnJson(entry);
          printlnJson(mdlReader.header);
          return;
        }
      }
    }
  }
  
  private Resource getResource(KeyReader.KeyEntry entry) {
    int bifIndex = entry.getBifIndex();
    int resourceIndex = entry.getResourceIndex();
    BifReader bifReader = bifReaders.get(bifIndex);
    EntryHeader entryHeader = bifReader.readEntryHeader(resourceIndex); 
    return new Resource(bifReader, entryHeader.offset, (int)entryHeader.size);
  }

  private void writeEntry(Resource resource, File out) {
    byte[] bytes = resource.reader.inp.readBytes(resource.offset, resource.length);
    try {
      Files.write(bytes, out);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
  }

}
