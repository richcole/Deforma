package game.models;

import game.enums.Res;

import java.io.File;

public class ResFiles {

  File getResFile(String resName, String resType) {
    return new File("res/" + resName + "." + resType.toLowerCase() + ".gz");
  }

  public File getResFile(Res res) {
    return getResFile(res.getResName(), res.getResType());
  }

}
