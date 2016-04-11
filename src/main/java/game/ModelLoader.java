package game;

import game.events.EventBus;
import game.format.DSReader;
import game.format.DSReader.Chunk;
import game.geom.Geom;
import game.image.Material;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModelLoader {
	 
	private static final Logger log = LoggerFactory.getLogger(ModelLoader.class);
	
	public List<Geom> load(EventBus eventBus, String filename, Material mat) {
		File file = new File(filename);
		DSReader reader = new DSReader(eventBus, file);
		Chunk root = reader.readChunk("");
		return reader.getMeshGeom(file.getParentFile(), root, mat);
	}

}
