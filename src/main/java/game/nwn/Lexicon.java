package game.nwn;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class Lexicon {
	
	Map<String, Integer> stringMap = Maps.newHashMap();
	Map<Integer, String> intMap = Maps.newHashMap();

	int getIndex(String string) {
		Integer index = stringMap.get(string);
		if ( index == null ) {
			index = stringMap.size();
			stringMap.put(string, index);
			intMap.put(index, string);
		}
		return index;
	}
	
	String getString(int i) {
		return intMap.get(i);
	}

	public List<String> toList() {
		List<String> result = Lists.newArrayList();
		for(int i=0; i<intMap.size(); ++i) {
			result.add(getString(i));
		}
		return result;
	}

	public int size() {
		return stringMap.size();
	}
}
