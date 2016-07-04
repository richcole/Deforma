package game.terrain;

public class TerrainBlockTypes {

	public String getImageName(byte terrainType) {
		if (terrainType == 0) {
			return null;
		}
		if (terrainType == 1) {
			return "marble.jpg";
		}
		throw new RuntimeException("Unknown terrain type");
	}
}
