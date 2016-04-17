package com.bricks.kvs.client;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * @author bricks <long1795@gmail.com>
 */
public class BricksPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {

	private KVClient kvClient;

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
		if (kvClient != null) {
			result = kvClient.get(placeholder);
		}
		return result;
	}

	public void setKvClient(KVClient kvClient) {
		this.kvClient = kvClient;
	}

}
