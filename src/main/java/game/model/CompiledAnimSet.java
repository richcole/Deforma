package game.model;

import game.math.Matrix;

import java.util.Map;

import com.google.common.collect.Maps;

public class CompiledAnimSet {

	private Map<String, CompiledMeshFrameList> map = Maps.newLinkedHashMap();
	
	public CompiledAnimSet() {
	}

	public void put(String animName, CompiledMeshFrameList compiledMeshFrameList) {
		map.put(animName, compiledMeshFrameList);
	}
	
	public void render(Matrix viewTr, Matrix modelTr, String animName, int frame, double t) {
		map.get(animName).render(viewTr, modelTr, frame, t);
	}
	
	public int getFrame(String animName, double t) {
		return map.get(animName).getFrame(t);
	}
}
