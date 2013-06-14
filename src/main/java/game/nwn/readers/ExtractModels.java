package game.nwn.readers;

import game.Context;
import game.base.Face;
import game.base.io.Serializer;
import game.models.AnimMesh;
import game.models.Model;
import game.nwn.NwnMesh;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.zip.GZIPOutputStream;

import org.apache.log4j.Logger;

import com.google.common.base.Throwables;
import com.google.common.io.Files;
import com.google.gson.GsonBuilder;

public class ExtractModels {
  
  private static final Logger logger = Logger.getLogger(ExtractModels.class);

  Context context;
  
  ExtractModels(Context context) {
    this.context = context;
  }

  public void println(String string) {
    System.out.println(string);
  }
  
  public void printlnJson(Object obj) {
    System.out.println(context.getGson().toJson(obj));
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
    byte[] bytes = resource.reader.inp.readBytes(resource.offset, resource.length);
    try {
      Files.write(bytes, out);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
  }

}
