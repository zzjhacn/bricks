package com.bricks.kvs.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import com.bricks.core.event.Event;
import com.bricks.core.simpleservice.DubboBasedServiceClient;
import com.bricks.kvs.KVStore;

/**
 * @author bricks <long1795@gmail.com>
 */
public class KVClientImpl extends DubboBasedServiceClient implements KVClient {

	@Resource
	private KVStore kvStore;

	private String scope;

	protected static final ConcurrentHashMap<String, String> cache = new ConcurrentHashMap<>();

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.kvs.client.KVClient#get(java.lang.String)
	 */
	@Override
	public String get(String key) {
		if (cache.containsKey(key)) {
			return cache.get(key);
		}
		String result = kvStore.get(scope, key);
		cache.put(key, result);
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.simpleservice.DubboBasedServiceClient#eventType()
	 */
	@Override
	protected String eventType() {
		return KVStore.EVENT_TYPE_CACHE_REFRESHED;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.simpleservice.DubboBasedServiceClient#handleImpl(com.bricks.core.event.Event)
	 */
	@Override
	protected void handleImpl(Event event) {
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> list = (Map<String, String>) event.getCtxVal(KVStore.EVENT_KEY_CACHE_REFRESHED);
			if (list.containsKey(KVStore.EVENT_KEY_CLEAR_CACHE)) {
				synchronized (cache) {
					cache.clear();
				}
				log().info("Cache cleared!");
				return;
			}
			if (list == null || list.isEmpty()) {
				log().warn("None data got in event context.");
			} else {
				synchronized (cache) {
					cache.clear();
					cache.putAll(list);
				}
			}
		} catch (Throwable e) {
			log().error("Error when handling event.", e);
		}
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

}
