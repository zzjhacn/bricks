package com.bricks.core.simpleservice;

import javax.annotation.PostConstruct;

import com.alibaba.dubbo.config.ApplicationConfig;
import com.alibaba.dubbo.config.RegistryConfig;
import com.bricks.core.SpringCtxHolder;
import com.bricks.core.event.Event;
import com.bricks.core.event.EventSubscriber;
import com.bricks.lang.log.LogAble;

/**
 * @author bricks <long1795@gmail.com>
 */
public abstract class SimpleServiceClient implements EventSubscriber, LogAble {

	abstract protected String eventType();

	abstract protected void handleImpl(Event event);

	private SimpleServiceServer simpleServiceServer;

	@PostConstruct
	public void init() {
		new Thread(() -> {
			try {
				Thread.sleep(1000);
			} catch (Throwable e) {}
			simpleServiceServer.registDubboSubscriber(eventType(), SpringCtxHolder.getBean(ApplicationConfig.class), SpringCtxHolder.getBean(RegistryConfig.class));
		}).start();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.bricks.core.event.EventSubscriber#handle(com.bricks.core.event.Event)
	 */
	@Override
	public void handle(Event event) {
		if (eventType().equals(event.getEventType())) {
			log().info("Handling event[{}].", event);
			handleImpl(event);
		}
	}

	public void setSimpleServiceServer(SimpleServiceServer simpleServiceServer) {
		this.simpleServiceServer = simpleServiceServer;
	}
}
