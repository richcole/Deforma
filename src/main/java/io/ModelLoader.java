package io;

import java.io.File;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Maps;

import game.ImageResource;
import game.voxel.MeshGeom;
import io.DSReader.Chunk;

public class ModelLoader {
	
	private static final Logger log = LoggerFactory.getLogger(ModelLoader.class);
	
	public static void main(String[] args) {
		new ModelLoader().load("/home/richcole/models/Girl/girl.3ds");
	}

	public MeshGeom load(String filename) {
		DSReader reader = new DSReader(new File(filename));
		Chunk root = reader.readChunk();
		return reader.getMeshGeom(root);
	}

}
