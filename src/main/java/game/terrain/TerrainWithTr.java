package game.terrain;

import game.math.Matrix;
import org.immutables.value.Value;

@Value.Immutable
@Value.Style(visibility = Value.Style.ImplementationVisibility.PRIVATE)
public interface TerrainWithTr {
	Matrix getTr();
	Terrain getTerrain();
}

