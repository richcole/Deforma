package game.nwn.readers;

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
}