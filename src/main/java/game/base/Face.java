package game.base;

import game.math.Vector;

public class Face {
  
  Vector[] vertices;
  Vector[] colors;
  Vector   specular;
  Vector   diffuse;
  Vector   normal;
  String   textureName;
  Vector[] texturePoints;
  
  public Face() {
  }

  public Face(Vector[] vertices, Vector[] colors, Vector diffuse, Vector specular, Vector normal, String textureName, Vector[] tps) {
    this.vertices = vertices;
    this.colors = colors;
    this.diffuse = diffuse;
    this.specular = specular;
    this.normal = normal;
    this.textureName = textureName;
    this.texturePoints = tps;
  }

  public Vector[] getVertices() {
    return vertices;
  }

  public Vector getSpecular() {
    return specular;
  }

  public void setSpecular(Vector specular) {
    this.specular = specular;
  }

  public Vector getDiffuse() {
    return diffuse;
  }

  public void setDiffuse(Vector diffuse) {
    this.diffuse = diffuse;
  }

  public Vector getNormal() {
    return normal;
  }

  public void setNormal(Vector normal) {
    this.normal = normal;
  }

  public void setVertices(Vector[] vertices) {
    this.vertices = vertices;
  }
  
  public String getTextureName() {
    return this.textureName;
  }

  public Vector[] getTexturePoints() {
    return this.texturePoints;
  }

  public Vector[] getColors() {
    return colors;
  }
  
}
