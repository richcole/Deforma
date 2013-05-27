package game.nwn;

import java.io.File;

public class KeyReader {
  
  BinaryFileReader inp;
  Header header;

  KeyReader(File bifFile) {
    inp = new BinaryFileReader(bifFile);
    header = readHeader();
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
