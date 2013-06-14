package game.nwn.readers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.Writer;

import game.Context;

import org.apache.log4j.Logger;

import com.google.common.base.Throwables;

public class ListModels {
  
  private static final Logger logger = Logger.getLogger(ListModels.class);

  Context context;
  
  ListModels(Context context) {
    this.context = context;
  }

  public void println(String string) {
    System.out.println(string);
  }
  
  public void printlnJson(Object obj) {
    System.out.println(context.getGson().toJson(obj));
  }
  
  public static void main(String[] args) {
    new ListModels(new Context()).run();
  }
  
  public void run() {
    logger.info("Starting");
    KeyReader keyReader = context.getKeyReader();
    try {
      PrintStream out = new PrintStream(new FileOutputStream(new File("list")));
      try {
        for(Resource resource: keyReader.getKeyIndex().values()) {
          ResourceType type = ResourceType.getType(resource.entry.type);
          out.println(resource.entry.name + " " + (type != null ? type.name() : resource.entry.type));
        }
      } finally {
        out.close();
      }
    } catch(Exception e) {
      Throwables.propagate(e);
    }
  }

}
