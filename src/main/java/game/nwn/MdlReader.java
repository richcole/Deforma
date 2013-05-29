package game.nwn;

import game.math.Vector;

import java.io.Closeable;
import java.util.Map;

import com.google.common.collect.Maps;

// note that this model reader is not correct I couldn't find an accurate description
// of the NWN model format
public class MdlReader implements Closeable {

  Resource resource;
  Header header;
  BinaryFileReader inp;
  Map<Long, ModelNodeHeader> modelNodeHeaders = Maps.newHashMap();
  Map<Long, MdlGeometryHeader> geometryHeaders = Maps.newHashMap();
  Map<Long, MdlModel> mdlModels = Maps.newHashMap();

  MdlReader(Resource resource) {
    this.resource = resource;
    this.inp = resource.reader.inp;
    this.header = readHeader();
  }
  
  static public class ArrayRef {
    long offset;
    int count;
    long alloc;
  }
  
  static public class MdlReferenceNode {
    String refModel;
    long reattachable;
  }
  
  static public class Face {
    Vector planeNormal;
    float planeDistance;
    long surface;
    int[] adjFace;
    Object vertex;
  }
  
  static public class MdlAnimationEvent {
    float after;
    String name;
  }
  
  static public class MdlAnimation {
    MdlGeometryHeader geometryHeader;
    float length;
    float transTime;
    String animRoot;
    MdlAnimationEvent[] events;
  }
  
  static public class MdlMeshHeader {
    long[]       meshRoutines;      
    Face[]       faces;
    Vector       bMin;
    Vector       bMax;
    float        radius;
    Vector       bAverage;
    Vector       diffuse;
    Vector       ambient;
    Vector       specular;
    float        shininess;
    long         shadow;
    long         beaming;
    long         render;
    long         transparencyHint;
    long         unknown5;
    String[]     textures;
    long         tileFade;
    long[]       vertexIndices;
    long[]       leftOverFaces;
    long         vertexIndicesCount;
    long[]       rawVertexIndices;
    long         something3Offset;
    long         something3Count;
    int          triangleMode;
    // pad 3 + 4
    Vector[]     vertices;
    long         vertexCount;
    long         textureCount;
  }
  
  static public class ModelNodeHeader {
    long[] nodeRoutines;
    long inheritColor;
    long partNumber;
    String name;
    MdlGeometryHeader geomemtryHeader;
    ModelNodeHeader parentNode;
    ModelNodeHeader[] children;
    MdlControllerKey[] controllerKey;
    float[] controllerData;
    long flags;
    MdlNodeType nodeType;
    MdlMeshHeader meshHeader;
    MdlReferenceNode referenceNode;
  }
  
  static public class MdlGeometryHeader {
    long[] aulGeomRoutines;
    String name;
    ModelNodeHeader geometry;
    Long nodeCount;
    long[] rtArray1;
    long[] rtArray2;
    long  u2;
    long geomType;
  }
  
  static public class MdlModel {
    MdlGeometryHeader geometryHeader;
    int       aucFlags; 
    long      classification;
    int       fog;
    long      refCount;
    MdlAnimation[] animations;
    MdlModel  superModel;
    long[]    bb;
    float     radius;
    float     animScale;
    String    superModelName;
  }
  
  static public class Header {
    long      zero;
    long      dataOffset;
    long      dataSize;
    MdlModel  model;
  }
  
  public static class MdlControllerKey {
    long type;
    int  rows;
    int  keyOffset;
    int  dataOffset;
    int  columns;
    int  pad;
  }
  
  public MdlAnimationEvent readMdlAnimationEvent() {
    MdlAnimationEvent r = new MdlAnimationEvent();
    r.after = inp.readFloat();
    r.name = inp.readNullString(32);
    return r;
  }
  
  public MdlAnimation readMdlAnimation() {
    MdlAnimation r = new MdlAnimation();
    r.length = inp.readFloat();
    r.transTime = inp.readFloat();
    r.animRoot = inp.readString(64);
    r.events  = readMdlAnimationEventList();
    return r;
  }

  MdlAnimationEvent[] readMdlAnimationEventList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    MdlAnimationEvent[] r = new MdlAnimationEvent[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*36);
      r[i] = readMdlAnimationEvent();
    }
    inp.seek(mark);
    return r;
  }

  MdlAnimation[] readIndirectMdlAnimationList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    MdlAnimation[] r = new MdlAnimation[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      seekOffset(inp.readWord());
      r[i] = readMdlAnimation();
    }
    inp.seek(mark);
    return r;
  }

  public ArrayRef readArrayRef() {
    ArrayRef r = new ArrayRef();
    r.offset = inp.readWord();
    r.count = (int) inp.readWord();
    r.alloc = inp.readWord();
    return r;
  }
  
  MdlControllerKey[] readControllerKeyList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    MdlControllerKey[] r = new MdlControllerKey[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*16);
      r[i] = readControllerKey();
    }
    inp.seek(mark);
    return r;
  }
  
  MdlControllerKey readControllerKey() {
    MdlControllerKey r = new MdlControllerKey();
    r.type = inp.readWord();
    r.rows = inp.readShort();
    r.keyOffset = inp.readShort();
    r.dataOffset = inp.readShort();
    r.columns = inp.readByte();
    r.pad = inp.readByte();
    return r;
  }
  
  float[] readFloatList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    float[] r = new float[(int)arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      r[i] = inp.readFloat();
    }
    inp.seek(mark);
    return r;
  }
  
  Face[] readFaceList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    Face[] r = new Face[(int)arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      r[i] = readFace();
    }
    inp.seek(mark);
    return r;
  }
  
  Face readFace() {
    Face r = new Face();
    r.planeNormal = readVector();
    r.planeDistance = inp.readFloat();
    r.surface = inp.readWord();
    r.adjFace = inp.readSignedShorts(3);
    r.vertex  = inp.readShorts(3);
    return r;
  }
  
  Vector readVector() {
    float x = inp.readFloat();
    float y = inp.readFloat();
    float z = inp.readFloat();
    return new Vector(x, y, z, 1.0);
  }
  
  Vector[] readVectors(int len) {
    Vector r[] = new Vector[len];
    for(int i=0;i<len;++i) {
      r[i] = readVector();
    }
    return r;
  }
  
  Vector[] readIndirectVectorList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    Vector[] r = new Vector[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      long p = inp.readWord();
      if ( p != 0xFFFFFFFF) {
        seekOffset(p);
        r[i] = readVector();
      }
    }
    inp.seek(mark);
    return r;
  }
  
  Vector[] readIndirectVectors(int len) {
    Vector[] r = new Vector[len];
    for(int i=0;i<len;++i) {
      long p = inp.readWord();
      if ( p != 0xFFFFFFFF) {
        long mark = seekOffset(p);
        r[i] = readVector();
        inp.seek(mark);
      }
    }
    return r;
  }
  
  Vector[] readIndirect2Vectors(int len) {
    Vector[] r = new Vector[len];
    for(int i=0;i<len;++i) {
      r[i] = readIndirect2Vector();
    }
    return r;
  }
  
  Vector readIndirect2Vector() {
    long p = inp.readWord();
    long mark = seekOffset(p);
    float x = inp.readFloat();
    float y = inp.readFloat();
    Vector r = new Vector(x, y, 0, 1.0);
    inp.seek(mark);
    return r;
  }

  Vector readIndirectVector() {
    long p = inp.readWord();
    long mark = seekOffset(p);
    Vector r = readVector();
    inp.seek(mark);
    return r;
  }

  long readIndirectWord() {
    long p = inp.readWord();
    long mark = seekOffset(p);
    long r = inp.readWord();
    inp.seek(mark);
    return r;
  }

  float readIndirectFloat() {
    long p = inp.readWord();
    long mark = seekOffset(p);
    float r = inp.readFloat();
    inp.seek(mark);
    return r;
  }

  ModelNodeHeader[] readModelNodeHeaderList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    ModelNodeHeader[] r = new ModelNodeHeader[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      r[i] = readModelNodeHeader(inp.readWord());
    }
    inp.seek(mark);
    return r;
  }
  
  long[] readWordList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    long[] r = new long[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      r[i] = inp.readWord();
    }
    inp.seek(mark);
    return r;
  }

  long[] readIndirectWordList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    long[] r = new long[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      seekOffset(inp.readWord());
      r[i] = inp.readWord();
    }
    inp.seek(mark);
    return r;
  }

  long[] readIndirectShortList() {
    ArrayRef arrayRef = readArrayRef();
    long mark = inp.pos();
    long[] r = new long[arrayRef.count];
    for(int i=0;i<arrayRef.count;++i) {
      seekOffset(arrayRef.offset + i*4);
      seekOffset(inp.readWord());
      r[i] = inp.readShort();
    }
    inp.seek(mark);
    return r;
  }

  public Header readHeader() {
    inp.seek(resource.offset);
    Header r = new Header();
    r.zero = inp.readWord();
    if ( r.zero != 0 ) {
      return r;
    }
    r.dataOffset = inp.readWord();
    r.dataSize = inp.readWord();
    r.model = readMdlModel();
    return r;
  }
  
  long seekOffset(long offset) {
    long mark = inp.pos();
    if ( offset >= resource.length ) {
      throw new RuntimeException("Overrun offset=" + offset);
    }
    long seekPos = offset + resource.offset + 12;
    inp.seek(seekPos);
    return mark;
  }
  
  public ModelNodeHeader readModelNodeHeader(Long offset) {
    if ( offset == 0 ) {
      return null;
    }
    ModelNodeHeader r = modelNodeHeaders.get(offset);
    if ( r == null ) {
      long mark = seekOffset(offset);
      modelNodeHeaders.put(offset, r);
      r = readModelNodeHeader();
      inp.seek(mark);
    }
    return r;
  }

  private ModelNodeHeader readModelNodeHeader() {
    ModelNodeHeader r;
    r = new ModelNodeHeader();
    r.nodeRoutines = inp.readWords(6);
    r.inheritColor = inp.readWord();
    r.partNumber = inp.readWord();
    r.name = inp.readNullString(32);
    r.geomemtryHeader = readGeomHeader(inp.readWord());
    r.parentNode = readModelNodeHeader(inp.readWord());
    r.children = readModelNodeHeaderList();
    r.controllerKey = readControllerKeyList();
    r.controllerData = readFloatList();
    r.flags = inp.readWord();
    r.nodeType = MdlNodeType.getMdlNodeType(r.flags);
    if ( r.nodeType.hasMesh() ) {
      r.meshHeader = readMdlMeshHeader();
    }
    if ( r.nodeType == MdlNodeType.Ref ) {
      r.referenceNode = readMdlReferenceNode();
    }
    return r;
  }
  
  private MdlReferenceNode readMdlReferenceNode() {
    MdlReferenceNode r = new MdlReferenceNode();
    r.refModel = inp.readNullString(64);
    r.reattachable = inp.readWord();
    return r;
  }
  
  private MdlMeshHeader readMdlMeshHeader() {
    MdlMeshHeader r = new MdlMeshHeader();
    r.meshRoutines = inp.readWords(2);
    r.faces = readFaceList();
    r.bMin = readVector();
    r.bMax = readVector();
    r.radius = inp.readFloat();
    r.bAverage = readVector();
    r.diffuse = readVector();
    r.ambient = readVector();
    r.specular = readVector();
    r.shininess = inp.readFloat();
    r.shadow = inp.readWord();
    r.beaming = inp.readWord();
    r.render = inp.readWord();
    r.transparencyHint = inp.readWord();
    r.unknown5 = inp.readWord();
    r.textures = inp.readNullStrings(64, 4);
    r.tileFade = inp.readWord();
    r.vertexIndices = readIndirectWordList();
    r.leftOverFaces = readWordList();
    r.vertexIndices = readWordList();
    r.rawVertexIndices = readIndirectShortList();
    r.something3Offset = inp.readWord();
    r.something3Count = inp.readWord();
    r.triangleMode = inp.readByte();
    inp.readBytes(7);
    long vertPointer = inp.readWord();
    r.vertexCount = inp.readShort();
    r.vertices = readVectorListAt(vertPointer, (int) r.vertexCount);
    r.textureCount = inp.readShort();
    return r;
  }
  
  public Vector[] readVectorListAt(long offset, int len) {
    Vector[] r = new Vector[len];
    long mark = seekOffset(offset);
    for(int i=0;i<len;++i) {
      r[i] = readVector();
    }
    inp.seek(mark);
    return r;
  }

  public MdlGeometryHeader readGeomHeader(long offset) {
    if ( offset == 0 ) {
      return null;
    }
    long mark = seekOffset(offset);
    MdlGeometryHeader r = geometryHeaders.get(offset);
    if ( r == null ) { 
      r = readMdlGeometryHeader();
      geometryHeaders.put(offset, r);
    }
    inp.seek(mark);
    return r;
  }
  
  public MdlGeometryHeader readMdlGeometryHeader() {
    MdlGeometryHeader r = new MdlGeometryHeader();
    r.aulGeomRoutines = inp.readWords(2);
    r.name   = inp.readNullString(64);
    r.geometry = readModelNodeHeader(inp.readWord());
    r.nodeCount = inp.readWord();
    r.rtArray1  = readWordList();
    r.rtArray2  = readWordList();
    r.u2        = inp.readWord();
    r.geomType  = inp.readWord();
    return r;
  }
  
  public MdlModel readMdlModel() {
    MdlModel r = new MdlModel();
    r.geometryHeader = readMdlGeometryHeader();
    r.aucFlags = inp.readShort();
    r.classification = inp.readByte();
    r.fog = inp.readByte();
    r.refCount = inp.readWord();
    r.animations = readIndirectMdlAnimationList();
    r.superModel = readMdlModel(inp.readWord());
    r.bb = inp.readWords(6);
    r.radius = inp.readFloat();
    r.animScale = inp.readFloat();
    r.superModelName = inp.readNullString(64);
    return r;
  }
  
  public MdlModel readMdlModel(long offset) {
    if ( offset == 0 ) {
      return null;
    }
    long mark = seekOffset(offset);
    MdlModel r = mdlModels.get(offset);
    if ( r == null ) { 
      r = readMdlModel();
      mdlModels.put(offset, r);
    }
    inp.seek(mark);
    return r;
  }
  
  private void assertPos(long offset) {
    long currentOffset = inp.pos() - resource.offset;
    if ( currentOffset != offset ) {
      throw new RuntimeException("Expected offset " + offset + " but found " + currentOffset);
    }
  }

  public void close() {
    inp.close();
  }
  
}
