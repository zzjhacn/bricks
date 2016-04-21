package com.bricks.kvs.client;

import java.util.Properties;

import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public class BricksPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer implements LogAble {

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
		}else{
			log().error("----------------------------null kv client---------------------------------");
		}
		return result;
	}

	public void setKvClient(KVClient kvClient) {
		this.kvClient = kvClient;
	}

}
