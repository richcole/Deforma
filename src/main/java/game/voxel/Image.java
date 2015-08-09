package game.voxel;

import java.nio.ByteBuffer;

public interface Image {

	ByteBuffer getRGBA();

	int getHeight();

	int getWidth();

}