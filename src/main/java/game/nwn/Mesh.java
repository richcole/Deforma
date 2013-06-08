package game.nwn;

import game.Context;
import game.base.Face;
import game.nwn.readers.Header;
import game.nwn.readers.MdlAnimation;
import game.nwn.readers.MdlGeometryHeader;
import game.nwn.readers.MdlNodeHeader;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;

public class Mesh {
  
  Context context;
  Header header;
    
  interface Visitor {
    void preVisit(MdlNodeHeader node, MdlNodeHeader fromNode, float alpha);
    void postVisit(MdlNodeHeader node, MdlNodeHeader fromNode, float alpha);
  }

  public Mesh(Context context, Header header, int x) {
    this.context = context;
    this.header = header;
  }

  private void visit(MdlNodeHeader geometry, boolean fromMatched, String fromAnimRoot, MdlNodeHeader fromGeom, float alpha, Visitor visitor) {
    fromMatched = fromMatched || fromAnimRoot.equals(geometry.getName());
    visitor.preVisit(geometry, fromMatched ? fromGeom : null, alpha);
    MdlNodeHeader[] c1 = geometry.getChildren();
    MdlNodeHeader[] c2 = fromGeom.getChildren();
    for(int i=0;i<c1.length;++i) {
      visit(c1[i], fromMatched, fromAnimRoot, fromMatched ? c2[i] : fromGeom, alpha, visitor);
    }
    visitor.postVisit(geometry, fromMatched ? fromGeom : null, alpha);
  }

  public List<Face> getFaces(String name, float alpha) {
    MdlAnimation anim = header.getModel().getAnimMap().get(name);
    alpha = alpha * anim.getLength();
    PlaneCollector planeCollector = new PlaneCollector(context);
    visit(header.getModel().getGeometryHeader().getGeometry(), false, anim.getAnimRoot(), anim.getGeometryHeader().getGeometry(), alpha, planeCollector);
    return planeCollector.getFaces();
  }
  
  public Set<Integer> getNumberOfFrames(String name) {
    MdlAnimation anim = header.getModel().getAnimMap().get(name);
    Set<Integer> numberOfFrames = Sets.newHashSet();
    getNumberOfFrames(anim.getGeometryHeader(), numberOfFrames);
    return numberOfFrames;
  }

  private void getNumberOfFrames(MdlGeometryHeader header, Set<Integer> frames) {
    if ( header.getGeometry() != null && header.getGeometry().getPosition() != null ) {
      frames.add(header.getGeometry().getPosition().length);
    }
    if ( header.getGeometry() != null && header.getGeometry().getOrientation() != null ) {
      frames.add(header.getGeometry().getOrientation().length);
    }
    for(MdlNodeHeader child: header.getGeometry().getChildren()) {
      if ( child.getOrientation() != null ) {
        frames.add(child.getOrientation().length);
      }
      if ( child.getPosition() != null ) {
        frames.add(child.getPosition().length);
      }
      if ( child.getGeomemtryHeader() != null ) {
        getNumberOfFrames(child.getGeomemtryHeader(), frames);
      }
    }
  }
}
