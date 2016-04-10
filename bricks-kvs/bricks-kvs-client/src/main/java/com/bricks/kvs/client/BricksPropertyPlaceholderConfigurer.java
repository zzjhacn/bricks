package com.bricks.kvs.client;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.bricks.kvs.KVStore;

/**
 * @author bricks <long1795@gmail.com>
 */
public class BricksPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private KVStore kvClient;

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
		result = kvClient.get(placeholder);
		return result;
	}

	public void setKvClient(KVStore kvClient) {
		this.kvClient = kvClient;
	}

}
