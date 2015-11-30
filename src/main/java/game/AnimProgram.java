package game;

import java.util.List;

import org.lwjgl.opengl.GL20;

public class AnimProgram {

  private GLProgram program;

  // variable uniforms
  private int tr, animIndex, alpha;

  // vertex attribs
  private int vert, texCoords, bones;

  // preloaded uniforms
  private int numAnim, numBones, rotationTable, translationTable, boneParents;

  public void AnimProgram() {
    program = new GLProgram();
    program.attach(new GLShader(GL20.GL_VERTEX_SHADER).compile("anim.vert"));
    program.attach(new GLShader(GL20.GL_FRAGMENT_SHADER).compile("anim.frag"));
    program.link();
    program.setUniform("tex", 0);

    tr = program.getUniform("tr");
    animIndex = program.getUniform("animIndex");
    alpha = program.getUniform("alpha");

    vert = program.getAttrib("vert");
    texCoords = program.getAttrib("texCoords");
    texCoords = program.getAttrib("bones");

    numAnim = program.getUniform("numAnim");
    numBones = program.getUniform("numBones");
    rotationTable = program.getUniform("rotationTable");
    translationTable = program.getUniform("translationTable");
    boneParents = program.getUniform("boneParents");
  }

  public int getVert() {
    return vert;
  }

  public int getTr() {
    return tr;
  }

  public void use() {
    program.use();
  }

  public int getTexCoords() {
    return texCoords;
  }

  public int getTex() {
    return 0;
  }

  public GLProgram getProgram() {
    return program;
  }

  public int getAnimIndex() {
    return animIndex;
  }

  public int getAlpha() {
    return alpha;
  }

  public int getBones() {
    return bones;
  }

  public void setBones(int bones) {
    this.bones = bones;
  }

  public int getNumAnim() {
    return numAnim;
  }

  public int getNumBones() {
    return numBones;
  }

  public int getRotationTable() {
    return rotationTable;
  }

  public int getTranslationTable() {
    return translationTable;
  }

  public int getBoneParents() {
    return boneParents;
  }

  public void setBoneParents(List<Integer> data) {
    program.setUniformInts(boneParents, data);
  }

  public void setRotationTable(List<Float> data) {
    program.setUniformFloats(rotationTable, data);
  }

  public void setTranslationTable(List<Float> data) {
    program.setUniformFloats(translationTable, data);
  }

  public void setNumAnim(int value) {
    program.setUniform(numAnim, value);
  }

  public void setNumBones(int value) {
    program.setUniform(numBones, value);
  }

  public void setAnimIndex(int value) {
    program.setUniform(animIndex, value);
  }

  public void setAlpha(float value) {
    program.setUniform(alpha, value);
  }

  public void setTr(Matrix value) {
    program.setUniformFloats(tr, value.toBuf());
  }
}
