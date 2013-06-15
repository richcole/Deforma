package game.models;

import game.Context;
import game.base.io.Serializer;
import game.enums.Model;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

public class Models {
  
  public Map<String, AnimMesh> models = Maps.newHashMap();
  Context context;
  
  public Models(Context context) {
    this.context = context;
  }
  
  public AnimMesh getAnimMesh(Model model) {
    String resName = model.getResName();
    File resFile = context.getResFiles().getResFile(model);
    return getAnimMesh(resName, resFile);
  }

  public AnimMesh getAnimMesh(String resName, File resFile) {
    AnimMesh animMesh = models.get(resName);
    if ( animMesh == null ) {
      Serializer serializer = new Serializer();
      animMesh = serializer.deserialize(resFile, AnimMesh.class);
      models.put(resName, animMesh);
    }
    return animMesh;
  }

  public AnimMesh getAnimMesh(String resName) {
    return getAnimMesh(resName, context.getResFiles().getResFile(resName, "mdl"));
  }
}
