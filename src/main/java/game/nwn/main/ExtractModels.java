package game.nwn.main;

import game.Context;
import game.base.io.Serializer;
import game.models.AnimMesh;
import game.models.Model;
import game.nwn.NwnMesh;
import game.nwn.readers.MdlReader;
import game.nwn.readers.Resource;
import game.nwn.readers.ResourceType;

import java.io.File;

import org.apache.log4j.Logger;

import com.google.common.base.Throwables;
import com.google.common.io.Files;

public class ExtractModels {
  
  private static final Logger logger = Logger.getLogger(ExtractModels.class);

  Context context;
  
  ExtractModels(Context context) {
    this.context = context;
  }

  public static void main(String[] args) {
    new ExtractModels(new Context()).run();
  }
  
  public void run() {
    for(Model model: Model.values()) {
      String resName = model.getResName();
      MdlReader mdlReader = context.getKeyReader().getMdlReader(resName);
      String resFileName = "res/" + resName + ".mdl.gz";
      NwnMesh mesh = new NwnMesh(context, mdlReader.readModel(), 0);
      AnimMesh animMesh = mesh.getAnimMesh();
      Serializer serializer = new Serializer();
      logger.info("Writing " + resFileName);
      serializer.serialize(animMesh, new File(resFileName));
      for(String textureName: animMesh.getTextures()) {
        try {
          Resource textureResource = context.getKeyReader().getResource(textureName, ResourceType.TGA);
          String textureResFileName = "res/" + textureName + ".tga";
          logger.info("Writing " + textureResFileName);
          textureResource.writeEntry(new File(textureResFileName));
        }
        catch(Exception e) {
          logger.error("Unable to locate texture: " + textureName);
        }
      }
    }
  }

  public void writeEntry(Resource resource, File out) {
    byte[] bytes = resource.getReader().getInp().readBytes(resource.getOffset(), resource.getLength());
    try {
      Files.write(bytes, out);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
  }

}
