package game.nwn.readers;

import game.Context;
import game.base.Face;
import game.base.io.Serializer;
import game.models.AnimMesh;
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
    MdlReader mdlReader = context.getKeyReader().getMdlReader("c_wererat");
    NwnMesh mesh = new NwnMesh(context, mdlReader.readModel(), 0);
    AnimMesh animMesh = mesh.getAnimMesh();
    Serializer serializer = new Serializer();
    serializer.serialize(animMesh, new File("res/wererat.mdl.gz"));
    for(String textureName: animMesh.getTextures()) {
      Resource textureResource = context.getKeyReader().getResource(textureName, ResourceType.TGA);
      textureResource.writeEntry(new File("res/" + textureName + ".tga"));
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
