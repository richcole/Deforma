package game.model;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.base.Objects;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import game.math.Matrix;

public class CompiledAnimSet implements TransformRenderable {

	private Map<String, CompiledMeshFrameList> map = Maps.newLinkedHashMap();
	private String currentAnim;
	
	public CompiledAnimSet() {
	}

	public void put(String animName, CompiledMeshFrameList compiledMeshFrameList) {
		map.put(animName, compiledMeshFrameList);
		if ( currentAnim == null ) {
			currentAnim = animName;
		}
	}
	
	public void setCurrentAnim(String currentAnim) {
		this.currentAnim = currentAnim;
	}

	@Override
	public void render(Matrix viewTr, Matrix modelTr) {
		map.get(currentAnim).render(viewTr, modelTr);
	}
	
	public void advanceSeconds(double dt) {
		map.get(currentAnim).advanceSeconds(dt);
	}

	public void nextAnim() {
		List<String> animNames = Lists.<String>newArrayList(map.keySet());
		for(int i=0; i<animNames.size(); ++i) {
			if ( Objects.equal(currentAnim, animNames.get(i)) ) {
				int nextIndex = (i + 1) % animNames.size();
				currentAnim = animNames.get(nextIndex);
				return;
			}
		}
		
		currentAnim = animNames.get(0);
	}
}
