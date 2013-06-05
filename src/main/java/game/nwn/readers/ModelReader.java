package game.nwn.readers;

import game.Context;
import game.nwn.Mesh;

public class ModelReader {
  
  Context context;
  
  ModelReader(Context context) {
    this.context = context;
  }
  
  Mesh readModel(String name) {
    MdlReader mdlReader = context.getKeyReader().getMdlReader("rat");
    return new Mesh(context, mdlReader.header, 0);
  }

}
