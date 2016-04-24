package com.bricks.kvs.client;

import java.net.MalformedURLException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.bricks.kvs.KVStore;
import com.bricks.lang.log.LogAble;
import com.caucho.hessian.client.HessianProxyFactory;

/**
 * @author bricks <long1795@gmail.com>
 */
public class BricksPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements LogAble, KVClient {

	private KVStore kvStore;

	private String appName;

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
		if (kvStore != null) {
			String result = kvStore.get(appName, key);
			if (result != null) {
				cache.put(key, result);
			}
			return result;
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.beans.factory.config.PropertyPlaceholderConfigurer#resolvePlaceholder(java.lang.String, java.util.Properties)
	 */
	@Override
	protected String resolvePlaceholder(String placeholder, Properties props) {
		String result = super.resolvePlaceholder(placeholder, props);
		if (result != null) {
			return result;
		}
		result = get(placeholder);
		return result;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public void setKvStoreHessianAddr(String kvStoreHessianAddr) {
		HessianProxyFactory hpf = new HessianProxyFactory();
		try {
			this.kvStore = (KVStore) hpf.create(KVStore.class, kvStoreHessianAddr);
		} catch (MalformedURLException e) {
			err(e);
		}
	}
}
