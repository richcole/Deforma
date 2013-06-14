package game.models;

import game.Context;
import game.base.io.Serializer;

import java.util.Map;

import com.google.common.collect.Maps;

public class Models {
  
  public Map<Model, AnimMesh> models = Maps.newHashMap();
  Context context;
  
  public Models(Context context) {
    this.context = context;
  }
  
  AnimMesh getAnimMesh(Model model) {
    AnimMesh animMesh = models.get(model);
    if ( animMesh == null ) {
      Serializer serializer = new Serializer();
      animMesh = serializer.deserialize(model.getResFile(), AnimMesh.class);
      models.put(model, animMesh);
    }
    return animMesh;
  }

}
