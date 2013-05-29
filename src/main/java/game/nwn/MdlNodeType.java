package game.nwn;

public enum MdlNodeType {
  
  Dummy(  0x1   | 0x1),
  Light(  0x1   | 0x2),
  Emitter(0x1   | 0x4),
  Camera( 0x1   | 0x8),
  Ref(    0x1   | 0x10),
  Mesh(   0x1   | 0x20),
  Skin(   0x21  | 0x40),
  Anim(   0x21  | 0x80),
  Dangly( 0x21  | 0x100),
  AABB(   0x21  | 0x200),
  UNKNOWN(0x21  | 0x400),
  ;

  private long id;

  MdlNodeType(long id) {
    this.id = id;
  }
  
  public static MdlNodeType getMdlNodeType(long flags) {
    for(MdlNodeType type: values()) {
      if ( flags == type.id ) {
        return type;
      }
    }
    return UNKNOWN;
  }

  public boolean hasMesh() {
    return (id & 0x21) == 0x21;
  }
}
