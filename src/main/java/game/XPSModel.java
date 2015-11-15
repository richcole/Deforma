package game;

import java.io.File;
import java.util.List;

public class XPSModel {

	List<XPSMesh> meshes;
	List<XPSBone> bones;
	File root;

	public XPSModel(File root, List<XPSBone> bones, List<XPSMesh> meshes) {
		this.root = root;
		this.bones = bones;
		this.meshes = meshes;
	}

}
