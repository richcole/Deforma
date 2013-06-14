package game.models;

import java.io.File;

public enum Model {
  
  Wererat("c_wererat"),
  Tcn01_r10_01("tcn01_r10_01");
  
  private String resName;

  Model(String resName) {
    this.resName = resName;
  }
  
  public String getResName() {
    return resName;
  }
  
  public File getResFile() {
    return new File("res/" + resName + ".mdl.gz");
  }

}
