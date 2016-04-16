package game.model;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.collect.Maps;

public class AnimSet implements Iterable<Entry<String, MeshFrameList>> {
	
	Map<String, MeshFrameList> meshFrameListMap = Maps.newHashMap();

	public void put(String animName, MeshFrameList meshFrameList) {
		meshFrameListMap.put(animName, meshFrameList);
	}

	@Override
	public Iterator<Entry<String, MeshFrameList>> iterator() {
		return meshFrameListMap.entrySet().iterator();
	}

}
