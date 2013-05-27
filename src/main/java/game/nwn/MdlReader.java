package game.nwn;

import java.io.Closeable;

// note that this model reader is not correct I couldn't find an accurate description
// of the NWN model format
public class MdlReader implements Closeable {

  Resource resource;
  Header header;
  BinaryFileReader inp;

  MdlReader(Resource resource) {
    this.resource = resource;
    this.inp = resource.reader.inp;
    this.header = readHeader();
  }
  
  static public class Header {
    long      offset;
    long      size;
    GeoHeader geoHeader;
    int       u1;
    ModelType type;
    int       fogged;
    long      u2;
    long      h1;
    long      h2;
    long      h3;
    long      parentPtr;
    long[]    bb;
    long      radius;
    long      animScale;
    String    superModel;
  }
  
  static public class GeoHeader {
    long u1;
    long u2;
    long u3;
    String name;
    long rootNodeOffset;
    long nodeCount;
    long refCount;
    long type;
  }
  
  public Header readHeader() {
    inp.seek(resource.offset);
    Header header = new Header();
    header.offset = inp.readWord();
    header.size = inp.readWord();
    header.geoHeader = readGeoHeader();
    header.u1 = inp.readShort();
    inp.readByte();
    long pos = inp.tell() - resource.offset;
    header.type = ModelType.withId(inp.readByte());
    header.fogged = inp.readByte();
    header.u2 = inp.readWord();
    header.h1 = inp.readWord();
    header.h2 = inp.readWord();
    header.h3 = inp.readWord();
    header.parentPtr = inp.readWord();
    header.bb = inp.readWords(6);
    header.radius = inp.readWord();
    header.animScale = inp.readWord();
    header.superModel = inp.readNullString(64);
    return header;
  }
  
  public GeoHeader readGeoHeader() {
    GeoHeader geoHeader = new GeoHeader();
    geoHeader.u1 = inp.readWord();
    geoHeader.u2 = inp.readWord();
    geoHeader.u3 = inp.readWord();
    geoHeader.name = inp.readNullString(64);
    geoHeader.rootNodeOffset = inp.readWord();
    geoHeader.nodeCount = inp.readWord();
    inp.readBytes(24);
    geoHeader.refCount = inp.readWord();
    geoHeader.type = inp.readWord();
    return geoHeader;
  }

  public void close() {
    inp.close();
  }
  
}
