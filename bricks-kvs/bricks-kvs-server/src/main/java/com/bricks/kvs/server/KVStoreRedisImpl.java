package com.bricks.kvs.server;

import java.util.HashMap;

import javax.annotation.Resource;

import com.bricks.core.event.Event;
import com.bricks.core.event.EventBus;
import com.bricks.core.redis.RedisService;
import com.bricks.core.schedule.ann.Schedulable;
import com.bricks.core.simpleservice.DubboBasedServiceServer;
import com.bricks.kvs.KVStore;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
@Schedulable
public class KVStoreRedisImpl implements KVStore, DubboBasedServiceServer, LogAble {
	
	@Resource
	private RedisService redis;

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.kvs.KVStore#get(java.lang.String)
	 */
	@Override
	public String get(String scope, String key) {
		return redis.getJedis().get(scope + "-" + key);
	}

	@Schedulable(name = "refresh-kv-map-task")
	public void refreshAndNotify() {
		// TODO 暂时无法实现对不动订阅端的定制化广播，因此广播清空缓存
		Event event = new Event(EVENT_TYPE_CACHE_REFRESHED);
		event.addContext(EVENT_KEY_CACHE_REFRESHED, new HashMap<String, String>());
		event.addContext(EVENT_KEY_CLEAR_CACHE, "Y");
		EventBus.broadcast(event);
	}
}
