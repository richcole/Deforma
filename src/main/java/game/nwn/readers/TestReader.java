package game.nwn.readers;

import game.Context;
import game.nwn.Mesh;
import game.nwn.readers.BifReader.EntryHeader;

import java.io.File;
import java.util.Map;

import com.google.common.base.Throwables;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class TestReader {

  Context context;
  
  TestReader(Context context) {
    this.context = context;
  }

  public void println(String string) {
    System.out.println(string);
  }
  
  public void printlnJson(Object obj) {
    System.out.println(context.getGson().toJson(obj));
  }
  
  public static void main(String[] args) {
    new TestReader(new Context()).run();
  }
  
  public void run() {
    MdlReader mdlReader = context.getKeyReader().getMdlReader("rat");
    Mesh mesh = new Mesh(context, mdlReader.getHeader(), 0);
    printlnJson(mesh);
  }
  
  private void writeEntry(Resource resource, File out) {
    byte[] bytes = resource.reader.inp.readBytes(resource.offset, resource.length);
    try {
      Files.write(bytes, out);
    }
    catch(Exception e) {
      Throwables.propagate(e);
    }
  }

}
