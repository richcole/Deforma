package game.nwn.readers;

import java.io.File;

import com.google.common.base.Throwables;
import com.google.common.io.Files;

import game.nwn.readers.KeyReader.KeyEntry;

class Resource {
  BifReader reader;
  long offset;
  int length;
  KeyEntry entry;
  
  Resource(BifReader reader, long offset, int length, KeyEntry entry) {
    this.reader = reader;
    this.offset = offset;
    this.length = length;
    this.entry  = entry;
  }
  
  String getName() {
    return entry.name;
  }

  public void writeEntry(File out) {
    byte[] bytes = reader.inp.readBytes(offset, length);
    try {
      Files.write(bytes, out);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
  }

}