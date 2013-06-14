package game.nwn.readers;

import game.Context;

import org.apache.log4j.Logger;

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
    for(Resource resource: keyReader.getKeyIndex().values()) {
      if ( resource.entry.name.contains("tree") && ResourceType.MDL.id == resource.entry.type ) {
        logger.info(resource.entry.name);
      }
    }
  }

}
