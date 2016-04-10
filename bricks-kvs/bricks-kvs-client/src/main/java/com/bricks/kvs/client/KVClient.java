package com.bricks.kvs.client;

import java.util.concurrent.ConcurrentHashMap;

import com.bricks.kvs.KVStore;

/**
 * @author bricks <long1795@gmail.com>
 */
public class KVClient implements KVStore {

	private KVStore kvStore;

	protected static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.kvs.KVStore#get(java.lang.String)
	 */
	@Override
	public String get(String key) {
		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		String result = kvStore.get(key);
		cache.put(key, result);
		return result;
	}

	public void setKvStore(KVStore kvStore) {
		this.kvStore = kvStore;
	}

}
