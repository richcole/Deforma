package game.nwn;

class Resource {
  BifReader reader;
  long offset;
  int length;
  
  Resource(BifReader reader, long offset, int length) {
    this.reader = reader;
    this.offset = offset;
    this.length = length;
  }
}