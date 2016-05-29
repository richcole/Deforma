package game.creature;

import java.util.Map;
import java.util.function.Function;

import com.google.common.collect.Maps;

public class Cache<T> implements Function<String, T> {
	
	private Map<String, T> cacheMap = Maps.newHashMap();
	private Function<String, T> factory;
	
	public Cache(Function<String, T> factory) {
		this.factory = factory;
	}

	@Override
	public T apply(String name) {
		T t = cacheMap.get(name);
		if ( t == null ) {
			t = factory.apply(name);
			cacheMap.put(name, t);
		}
		return t;
	}

}
