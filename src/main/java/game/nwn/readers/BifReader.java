package game.nwn.readers;

import game.nwn.readers.KeyReader.BifEntry;

import java.io.Closeable;
import java.io.File;

public class BifReader implements Closeable {
  
  String bifFileName;
  BinaryFileReader inp;
  Header header;
  BifEntry entry;

  BifReader(BifEntry entry, File bifFile) {
    this.entry = entry;
    this.inp = new BinaryFileReader(bifFile);
    this.header = readHeader();
  }
  
  static public class Header {
    String sig;
    String version;
    long entries;
    long tiles;
    long entryOffset;
  }
  
  static public class EntryHeader {
    long ids;
    long offset;
    long size;
    long type;
    long unknown;
  }

  public Header readHeader() {
    inp.seek(0);
    Header header = new Header();
    header.sig = inp.readString(4);
    header.version = inp.readString(4);
    header.entries = inp.readWord();
    header.tiles = inp.readWord();
    header.entryOffset = inp.readWord();
    return header;
  }
  
  public EntryHeader readEntryHeader(int i) {
    inp.seek(header.entryOffset + i*16);
    EntryHeader entryHeader = new EntryHeader();
    entryHeader.ids = inp.readWord();
    entryHeader.offset = inp.readWord();
    entryHeader.size = inp.readWord();
    entryHeader.type = inp.readWord();
    return entryHeader;
  }

  public void close() {
    inp.close();
  }
  
}
