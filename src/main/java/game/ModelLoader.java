package game;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.DSReader.Chunk;

public class ModelLoader {
	 
	private static final Logger log = LoggerFactory.getLogger(ModelLoader.class);
	
	public List<Geom> load(String filename, Material mat) {
		File file = new File(filename);
		DSReader reader = new DSReader(file);
		Chunk root = reader.readChunk("");
		return reader.getMeshGeom(file.getParentFile(), root, mat);
	}

}
