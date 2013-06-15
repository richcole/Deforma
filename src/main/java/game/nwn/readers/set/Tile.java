package game.nwn.readers.set;

import java.util.List;

import com.google.common.collect.Lists;

public class Tile {
  String model;
  String walkMesh;
  String topLeft;
  String topLeftHeight;
  String topRight;
  String topRightHeight;
  String bottomLeft;
  String bottomLeftHeight;
  String bottomRight;
  String bottomRightHeight;
  String top;
  String right;
  String bottom;
  String left;
  String mainLight1;
  String mainLight2;
  String sourceLight1;
  String sourceLight2;
  String animLoop1;
  String animLoop2;
  String animLoop3;
  String sounds;
  String pathNode;
  String orientation;
  String imageMap2D;
  String doorVisibilityNode;
  String doorVisibilityOrientation;
  String visibilityNode;
  String visibilityOrientation;

  @Name("doors")
  Integer numDoors;
  
  @Name("doorsList")
  List<Door> doors = Lists.newArrayList();
}
