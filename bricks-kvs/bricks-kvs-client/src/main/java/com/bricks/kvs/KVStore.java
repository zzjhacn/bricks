package com.bricks.kvs;

/**
 * @author bricks <long1795@gmail.com>
 */
public interface KVStore {
	public static final String EVENT_TYPE_CACHE_REFRESHED = "EVENT_TYPE_CACHE_REFRESHED_KV";
	public static final String EVENT_KEY_CACHE_REFRESHED = "EVENT_KEY_KV_MAPS";
	public static final String EVENT_KEY_CLEAR_CACHE = "EVENT_KEY_CLEAR_CACHE";

	String get(String scope, String key);
}
