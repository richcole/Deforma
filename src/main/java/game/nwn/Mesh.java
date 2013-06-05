package game.nwn;

import game.base.Face;
import game.nwn.readers.Header;
import game.nwn.readers.KeyReader;
import game.nwn.readers.MdlAnimation;
import game.nwn.readers.MdlGeometryHeader;
import game.nwn.readers.MdlNodeHeader;
import game.Context;

import java.util.List;

import com.google.common.collect.Lists;

public class Mesh {
  
  Context context;
  Header header;
    
  interface Visitor {
    void preVisit(MdlNodeHeader node, MdlNodeHeader fromNode, MdlNodeHeader toNode, float alpha);
    void postVisit(MdlNodeHeader node, MdlNodeHeader fromNode, MdlNodeHeader toNode, float alpha);
  }

  public Mesh(Context context, Header header, int x) {
    this.context = context;
    this.header = header;
  }

  private void visit(MdlNodeHeader geometry, boolean fromMatched, String fromAnimRoot, MdlNodeHeader fromGeom, boolean toMatched, String toAnimRoot, MdlNodeHeader toGeom, float alpha, Visitor visitor) {
    fromMatched = fromMatched || fromAnimRoot.equals(geometry.getName());
    toMatched = toMatched || toAnimRoot.equals(geometry.getName());
    visitor.preVisit(geometry, fromMatched ? fromGeom : null, toMatched ? toGeom : null, alpha);
    MdlNodeHeader[] c1 = geometry.getChildren();
    MdlNodeHeader[] c2 = fromGeom.getChildren();
    MdlNodeHeader[] c3 = toGeom.getChildren();
    for(int i=0;i<c1.length;++i) {
      visit(c1[i], fromMatched, fromAnimRoot, fromMatched ? c2[i] : fromGeom, toMatched, toAnimRoot, toMatched ? c3[i] : toGeom, alpha, visitor);
    }
    visitor.postVisit(geometry, fromMatched ? fromGeom : null, toMatched ? toGeom : null, alpha);
  }

  public List<Face> getFaces(int fromAnimIndex, int toAnimIndex, float alpha) {
    MdlAnimation[] animations = header.getModel().getAnimations();
    MdlAnimation fromAnim = animations[fromAnimIndex % animations.length];
    MdlAnimation toAnim = animations[toAnimIndex % animations.length];
    PlaneCollector planeCollector = new PlaneCollector(context);
    visit(header.getModel().getGeometryHeader().getGeometry(), false, fromAnim.getAnimRoot(), fromAnim.getGeometryHeader().getGeometry(), false, toAnim.getAnimRoot(), toAnim.getGeometryHeader().getGeometry(), alpha, planeCollector);
    return planeCollector.getFaces();
  }

}
