package com.bricks.core.simpleservice;

import javax.annotation.PostConstruct;

import com.bricks.core.event.EventSubscriber;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class SimpleServiceClient implements EventSubscriber {

	abstract String eventType();

	private SimpleServiceServer simpleServiceServer;

	private String eventNotifyUrl;

	@PostConstruct
	public void init() {
		simpleServiceServer.registRemoteSubscriber(eventType(), getClass().getName(), eventNotifyUrl);
	}

}
