package game.models;

import static org.lwjgl.opengl.GL11.GL_QUADS;
import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor3d;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glTexCoord2f;
import static org.lwjgl.opengl.GL11.glVertex3d;

import game.Context;
import game.GrayCode;
import game.Renderable;
import game.Texture;
import game.math.Matrix;
import game.math.Vector;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.common.base.Function;
import com.google.common.collect.Lists;


public class Cube implements Renderable {
  
  private static Logger logger = Logger.getLogger(Cube.class);
  
  Context context;
  
  Vector size;
  Vector position;
 
  static final public List<Matrix> codes = toCodeMatrix(Lists.newArrayList( 
      new GrayCode(1, 2, 0,  1, 3,  1,  1), // 0 left
      new GrayCode(1, 2, 0, -1, 3,  1, -1), // 1 right
      new GrayCode(0, 2, 1,  1, 3,  1, -1), // 2 up
      new GrayCode(0, 2, 1, -1, 1,  1,  1), // 3 down
      new GrayCode(0, 1, 2,  1, 2,  1,  1), // 4 front
      new GrayCode(0, 1, 2, -1, 2, -1,  1)  // 5 back
  ));
  
  static final public Matrix textureCode = new GrayCode(0, 1, 2, -1, 0, 1, 1)
    .getCode().plus(Matrix.ONES).times(0.5);
  
  public Cube(Context context, Vector position, Vector size) {
    this.size = new Vector(size);
    this.position = new Vector(position);
    this.context = context;
    context.getScene().register(this);
  }
  
  public static List<Matrix> toCodeMatrix(ArrayList<GrayCode> codes) {
    return Lists.transform(codes, new Function<GrayCode, Matrix>() {
      @Override
      public Matrix apply(GrayCode code) {
        return code.getCode();
      } 
    });
  }

  public void render() {
    logger.info("Drawing cube");
    for(int i=0;i<6;++i) {
      getTexture(i).bind();
      Matrix code = getCode(i);
      glBegin(GL_QUADS);  
      glColor3d(1.0f, 1.0f, 1.0f);
      for(int j=0;j<4;++j) {
        logger.info("texture x=" + textureCode.get(j, 0) + ", y=" + textureCode.get(j, 1));
        glTexCoord2f((float)textureCode.get(j, 0), (float)textureCode.get(j, 1));
        logger.info("cube x=" + code.get(j, 0) + ", y=" + code.get(j, 1) + ", z=" + code.get(j, 2));
        glVertex3d(position.x() + code.get(j, 0)*size.x(), position.y() + code.get(j, 1)*size.y(), position.z() + code.get(j, 2)*size.z());
      }
      glEnd();
    }
  }

  public Matrix getCode(int i) {
    return codes.get(i);
  }

  public Texture getTexture(int i) {
    return context.getStoneTexture();
  }

  public void setPosition(Vector position) {
    this.position = new Vector(position);
  }
  
  public void setSize(Vector size) {
    this.size = new Vector(size);
  }

  public Vector getPosition() {
    return position;
  }

  public void move(Vector velocity) {
    position = position.plus(velocity);
  }
}
