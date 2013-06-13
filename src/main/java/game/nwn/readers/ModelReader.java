package game.nwn.readers;

import game.Context;
import game.nwn.NwnMesh;

public class ModelReader {
  
  Context context;
  
  ModelReader(Context context) {
    this.context = context;
  }
  
  NwnMesh readModel(String name) {
    MdlReader mdlReader = context.getKeyReader().getMdlReader("rat");
    return new NwnMesh(context, mdlReader.readMdlModel(), 0);
  }

}
