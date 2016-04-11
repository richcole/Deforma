package game.nwn;

import game.events.EventBus;
import game.geom.VertexCloud;
import game.mesh.CompiledMesh;
import game.nwn.readers.MdlFace;
import game.nwn.readers.MdlGeometryHeader;
import game.nwn.readers.MdlMeshHeader;
import game.nwn.readers.MdlModel;
import game.nwn.readers.MdlNodeHeader;

public class NwnModelMeshBuilder {

  public CompiledMesh compile(EventBus eventBus, MdlModel model) {
    MdlGeometryHeader hdr = model.getGeometryHeader();
    MdlNodeHeader geom = hdr.getGeometry();
    VertexCloud vertexCloud = new VertexCloud(null);
    collectFaces(geom, vertexCloud);
    return new CompiledMesh(eventBus, null, vertexCloud);
  }

  private void collectFaces(MdlNodeHeader geom, VertexCloud cloud) {
    for(MdlNodeHeader child: geom.getChildren()) {
      MdlMeshHeader mHdr = geom.getMeshHeader();
    }
  }
}
