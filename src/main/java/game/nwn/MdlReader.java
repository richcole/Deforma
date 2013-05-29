package game.nwn;

import game.math.Vector;

import java.io.Closeable;
import java.util.List;
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
  
  static public class Face {

    public Vector planeNormal;
    public float planeDistance;
    public long surface;
    public int[] adjFace;
    public Object vertex;
    
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
    int          pad1;
    long         tempMeshData;
    Vector[]     m_pavVerts;
    int          vertexCount;
    int          textureCount;
    Vector[]     tVerts;
    Vector[]     normals;
    long[]       colors;
    Vector[]     bumpmapAnim1;
    Vector[]     bumpmapAnim2;
    Vector[]     bumpmapAnim3;
    Vector[]     bumpmapAnim4;
    Vector[]     bumpmapAnim5;
    float[]      bumpmapAnim6;
    int          lightMapped;
    int          rotateTexture;
    int          pad2;
    float        faceNormalSumDiv2;
    long         unknown1;
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
  }
  
  static public class MdlGeometryHeader {
    long[] aulGeomRoutines;
    String name;
    ModelNodeHeader geometry;
    Long nodeCount;
    ArrayRef rtArray1;
    ArrayRef rtArray2;
    long  u2;
    long geomType;
  }
  
  static public class MdlModel {
    MdlGeometryHeader geometryHeader;
    int       aucFlags; 
    long      classification;
    int       fog;
    long      refCount;
    ArrayRef  animations;
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
  
  public ArrayRef readArrayRef() {
    ArrayRef r = new ArrayRef();
    r.offset = inp.readWord();
    r.count = (int) inp.readWord();
    r.alloc = inp.readWord();
    return r;
  }
  
  class MdlControllerKey {
    long type;
    int  rows;
    int  keyOffset;
    int  dataOffset;
    int  columns;
    int  pad;
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
    return r;
  }
  
  private MdlMeshHeader readMdlMeshHeader() {
    MdlMeshHeader r = new MdlMeshHeader();
    r.meshRoutines = inp.readWords(2);
    r.faces = readFaceList();
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
    r.rtArray1  = readArrayRef();
    r.rtArray2  = readArrayRef();
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
    r.animations = readArrayRef();
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
